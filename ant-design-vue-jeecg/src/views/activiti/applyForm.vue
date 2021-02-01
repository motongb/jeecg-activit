<template>
  <div>
    <a-card :body-style="{padding: '24px 32px'}" :bordered="false">
      <a-button style="margin-left: 8px" type="primary" ghost @click="closed">返 回</a-button>
      <!--流程表单-->
      <component :disabled="lcModa.disabled" :is="lcModa.formComponent"
                 :processData="lcModa.processData" :isNew="lcModa.isNew"
                 :task="lcModa.isTask"
                 @afterSubmit="afterSub"></component>

      <a-form-item v-if="lcModa.isTask" :wrapperCol="{ span: 24 }" style="text-align: center">
        <a-button type="primary" @click="passTask">通 过</a-button>
        <a-button style="margin-left: 8px" @click="backTask">驳 回</a-button>
      </a-form-item>
    </a-card>

    <sign-modal :form="form" :modal-task-title="modalTaskTitle" :modal-task-visible="modalTaskVisible"
                :assignee-list="assigneeList" :user-loading="userLoading" @cancel="modalTaskVisible = false"
                @afterSub="afterSub"
                :show-assign="showAssign" :back-loading="backLoading" :back-list="backList"></sign-modal>
  </div>
</template>

<script>
  import { getStore, clearStore } from '@/utils/storage'
  import { activitiMixin } from '@/views/activiti/mixins/activitiMixin'
  import SignModal from './signModal'

  export default {
    name: 'applyForm',
    components: { SignModal },
    mixins: [activitiMixin],
    data() {
      return {
        url: {
          getNextNode: '/activiti_process/getNextNode',
          getBackList: '/actTask/getBackList/'
        },
        backList: [
          {
            key: '-1',
            name: '发起人'
          }
        ],
        lcModa: {},
        modalTaskTitle: '',
        form: {
          id: '',
          userId: '',
          procInstId: '',
          comment: '',
          type: 0,
          assignees: [],
          backTaskKey: '-1',
          sendMessage: true,
          sendSms: false,
          sendEmail: false
        },
        modalTaskVisible: false,
        userLoading: false,
        assigneeList: [],
        showAssign: true,
        backLoading: false
      }
    },
    mounted() {
      let params = getStore('lcModa')
      this.lcModa = params
      this.lcModa.formComponent = this.getFormComponent(params.processData.routeName).component
      console.log('lcModa',this.lcModa)
    },
    methods: {
      /*通过审批*/
      passTask() {
        let v = this.lcModa.processData
        this.modalTaskTitle = '审批通过'
        this.form.id = v.id
        this.form.procInstId = v.procInstId
        this.form.priority = v.priority
        this.form.type = 0
        this.userLoading = true
        this.postFormAction(this.url.getNextNode, {
          procDefId: v.procDefId,
          currActId: v.key,
          procInstId: v.procInstId
        }).then(res => {
          this.userLoading = false
          if (res.success) {
            this.assigneeList = res.result
            this.showAssign = true
            if (res.result.length === 0 || res.result[0].type === 2) {
              this.showAssign = false
            }
            this.modalTaskVisible = true
          }
        })
      },
      /*驳回审批*/
      backTask() {
        let v = this.lcModa.processData
        this.modalTaskTitle = '审批驳回'
        this.form.id = v.id
        this.form.procInstId = v.procInstId
        this.form.procDefId = v.procDefId
        this.form.priority = v.priority
        this.form.type = 1
        this.showAssign = false
        this.modalTaskVisible = true
        // 获取可驳回节点
        this.backList = [
          {
            key: '-1',
            name: '发起人'
          }
        ]
        this.form.backTaskKey = '-1'
        this.backLoading = true
        this.getAction(this.url.getBackList + v.procInstId).then(res => {
          this.backLoading = false
          if (res.success) {
            res.result.forEach(e => {
              this.backList.push(e)
            })
          }
          console.log('驳回人列表', this.backList)
        })
      },
      afterSub(formData) {
        this.closed()
        this.modalTaskVisible = false
      },
      closed() {
        clearStore('lcModa')
        this.$router.push(this.lcModa.from)
      }
    }
  }
</script>

<style scoped>

</style>