<template>
  <div>
    <comment-modal v-if="showCommentVisible" @closed="showCommentVisible=false"
                   :bindId="lcModa.processData?lcModa.processData.tableId:''"
                   :title="lcModa.title" :bind-user="lcModa.processData?lcModa.processData.createBy:''"></comment-modal>
    <notice-message :bindId="lcModa.processData?lcModa.processData.tableId:''" :title="lcModa.title" ref="noticeMessage"
                    :bind-user="lcModa.processData?lcModa.processData.createBy:''"
                    :business-key="lcModa.processData?lcModa.processData.businessKey:''"></notice-message>
    <a-card class="apply-card" title="申请信息">
      <div slot="extra">
        <a-button v-show="!lcModa.isNew" type="primary" ghost @click="showComment">查看评论
        </a-button>
        <template v-if="lcModa.isTask">
          <a-button style="margin-left: 8px" type="primary" ghost @click="needEdit">需要修改</a-button>
          <a-button style="margin-left: 8px" type="primary" @click="passTask">通 过</a-button>
          <a-button style="margin-left: 8px" @click="backTask">驳 回</a-button>
        </template>
        <a-button v-show="!lcModa.disabled&&!btndisabled" style="margin-left: 8px" type="primary" @click="handleSubmit">
          保存
        </a-button>
        <a-button style="margin-left: 8px" type="primary" ghost @click="closed">返 回</a-button>
      </div>
      <div class="apply-header">
        <j-form-container :disabled="lcModa.disabled">
          <a-form-model ref="ruleForm" slot="detail" :rules="rules" :model="lcModa" :label-col="labelCol"
                        :wrapper-col="wrapperCol">
            <a-row>
              <a-col :span="24">
                <a-form-model-item :label-col="{ span: 2 }" :wrapper-col="{span:20}" label="标题" prop="title">
                  <a-input v-model="lcModa.title" placeholder="请输入标题"/>
                </a-form-model-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="8">
                <a-form-model-item label="申请人">
                  <a-input disabled v-model="lcModa.userName" placeholder="请输入申请人"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="申请部门">
                  <a-input disabled v-model="lcModa.dept" placeholder="请输入申请部门"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="申请时间">
                  <a-input disabled v-model="lcModa.applyTime" placeholder="请输入申请时间"/>
                </a-form-model-item>
              </a-col>
            </a-row>
          </a-form-model>
        </j-form-container>
      </div>
    </a-card>

    <!--流程表单-->
    <component :is="lcModa.formComponent" ref="processForm" :lcModa="lcModa" :preView="false"></component>

    <sign-modal :form="form" :modal-task-title="modalTaskTitle" :modal-task-visible="modalTaskVisible"
                :assignee-list="assigneeList" :user-loading="userLoading" @cancel="modalTaskVisible = false"
                @afterSub="closed" :show-assign="showAssign" :back-loading="backLoading"
                :back-list="backList"></sign-modal>
  </div>
</template>

<script>
  import { getStore, clearStore } from '@/utils/storage'
  import { activitiMixin } from '@/views/activiti/mixins/activitiMixin'
  import SignModal from './signModal'
  import { formatDate } from '@/utils/util'
  import activitiSetting from './mixins/activitiSetting'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import { getAction } from '@/api/manage'
  import { postFormAction } from '@/api/localRequest'
  import CommentModal from './components/CommentModal'
  import NoticeMessage from './components/NoticeMessage'

  export default {
    name: 'applyForm',
    components: { NoticeMessage, CommentModal, SignModal, JFormContainer },
    mixins: [activitiMixin],
    data() {
      return {
        labelCol: { span: 4, offset: 2 },
        wrapperCol: { span: 12 },
        url: {
          getNextNode: '/activiti_process/getNextNode',
          getBackList: '/actTask/getBackList/',
          userWithDepart: '/sys/user/userDepartList',
          updateFix: '/actBusiness/updateFix'
        },
        backList: [
          {
            key: '-1',
            name: '发起人'
          }
        ],
        lcModa: {},
        modalTaskTitle: '',
        rules: {
          title: { required: true, message: '请输入标题', trigger: 'blur' }
        },
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
        showCommentVisible: false,
        modalTaskVisible: false,
        userLoading: false,
        assigneeList: [],
        showAssign: true,
        backLoading: false,
        btndisabled: false,
        userInfo: {}
      }
    },
    watch: {
      // 解决tabs切换页面不刷新问题
      '$route': function(newRoute) {
        let params = getStore('lcModa')
        if (params && params.reload) {
          this.$bus.$emit('reload-route')
        }
      }
    },
    created() {
      this.initValue()
    },
    methods: {
      needEdit() {
        this.$refs.noticeMessage.show()
      },
      initValue() {
        let params = getStore('lcModa')
        this.lcModa = params || {}
        console.log(params)
        if (params) {
          this.lcModa.formComponent = this.getFormComponent(params.processData.routeName,params.processData.formType)
          this.userInfo = this.$store.getters.userInfo
          this.lcModa.userName = this.userInfo.realname
          this.lcModa.applyTime = formatDate(new Date().getTime(), 'yyyy-MM-dd hh:mm:ss')
          if (!params.processData.dept) {
            this.getUserDepart()
          } else {
            this.lcModa.dept = params.processData.dept
          }
        }
      },
      getUserDepart() {
        getAction(this.url.userWithDepart, { userId: this.userInfo.id }).then(res => {
          if (res.success) {
            this.lcModa.dept = res.result.map(m => m.title).join('-')
            this.lcModa.title += '-' + this.lcModa.dept
          }
        })
      },
      showComment() {
        this.showCommentVisible = true
      },
      handleSubmit(e) {
        this.$refs.ruleForm.validate(valid => {
          if (valid) {
            this.btndisabled = true
            this.$refs.processForm.submitForm().then(res => {
              if (!this.lcModa.isTask) {
                this.afterSub(res)
              }
              this.btndisabled = false
              // 是否修订
              if (this.lcModa.isFix) {
                getAction(this.url.updateFix, { id: this.lcModa.processData.id, status: 2 })
              }
            }).catch(_ => {
              this.btndisabled = false
            })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      /*通过审批*/
      passTask() {
        let v = this.lcModa.processData
        this.modalTaskTitle = '审批通过'
        this.form.id = v.id
        this.form.procInstId = v.procInstId
        this.form.priority = v.priority
        this.form.type = 0
        this.userLoading = true
        postFormAction(this.url.getNextNode, {
          procDefId: v.procDefId,
          currActId: v.key,
          procInstId: v.procInstId
        }).then(res => {
          this.userLoading = false
          if (res.success) {
            this.assigneeList = res.result
            this.showAssign = true
            //没有下一处理人或最后节点
            if (res.result.length === 0 || res.result[0].type === 2) {
              this.showAssign = false
            } else {
              //默认选中候选人
              this.assigneeList.forEach(item => item.values = item.users.map(m => m.username))
            }
            this.modalTaskVisible = true
            console.log(this.assigneeList)
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
        getAction(this.url.getBackList + v.procInstId).then(res => {
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
        this.$router.push(activitiSetting.applyListPath)
        clearStore('lcModa')
        this.closedCurrentPage()
      },
      closed() {
        this.closedCurrentPage()
        this.$router.push(this.lcModa.from)
        this.modalTaskVisible = false
        clearStore('lcModa')
      },
      closedCurrentPage() {
        this.$bus.$emit('closed-current-tabs')
      }
    }
  }
</script>

<style scoped>
  .apply-header {
    margin-top: 20px;
  }

  .apply-card {
    /*margin-bottom: 24px;*/
  }
</style>                                                                                                                                                                                   