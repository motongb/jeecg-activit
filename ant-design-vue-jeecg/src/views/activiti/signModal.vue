<template>
  <div>
    <!-- 审批操作 -->
    <a-modal :title="modalTaskTitle" :visible="modalTaskVisible" @cancel="cancel" :width="500">
      <div>
        <a-form ref="form" :model="form" :label-width="85">
          <a-form-item label="审批意见" prop="reason">
            <a-input type="textarea" v-model="form.comment" :rows="4"/>
          </a-form-item>

          <template v-if="showAssign">
            <a-form-item v-for="(assignee,index) in assigneeList" :label="assignee.title" :error="error">
              <a-select
                :defaultValue="assignee.values"
                placeholder="请选择"
                allowClear
                @change="(selected)=>assignChangeHandle(index,selected)"
                mode="multiple">
                <a-select-option v-for="item in assignee.users" :key="item.id" :value="item.username">{{item.realname}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </template>

          <div v-show="form.type==1">
            <a-form-item label="驳回至">
              <a-select
                v-model="form.backTaskKey"
                :loading="backLoading"
                @change="changeBackTask">
                <a-select-option v-for="(item, i) in backList" :key="i" :value="item.key">{{item.name}}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="指定原节点审批人" prop="assignees" v-if="form.backTaskKey!=-1" :error="error">
              <a-select
                v-model="form.assignees"
                placeholder="请选择"
                allowClear
                mode="multiple"
                :loading="userLoading">
                <a-select-option v-for="(item, i) in assignees" :key="i" :value="item.username">{{item.realname}}
                </a-select-option>
              </a-select>
            </a-form-item>
          </div>
          <a-form-item label="选择委托人" prop="userId" :error="error" v-show="form.type==2">
            <JSelectUserByDep v-model="form.userId" :multi="false"></JSelectUserByDep>
          </a-form-item>
          <a-form-item label="消息通知">
            <a-checkbox v-model="form.sendMessage">站内消息通知</a-checkbox>
            <a-checkbox v-model="form.sendSms" disabled>短信通知</a-checkbox>
            <a-checkbox v-model="form.sendEmail" disabled>邮件通知</a-checkbox>
          </a-form-item>
        </a-form>
      </div>
      <div slot="footer">
        <a-button type="text" @click="cancel">取消</a-button>
        <a-button type="primary" :loading="submitLoading" @click="handelSubmit">提交</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  import { getStore, clearStore } from '@/utils/storage'
  import { getAction, postAction } from '@/api/manage'
  import { postFormAction } from '@/api/localRequest'

  export default {
    name: 'signModal',
    components: { JSelectUserByDep },
    props: {
      backLoading: {
        type: Boolean,
        default: false
      },
      modalTaskVisible: {
        type: Boolean,
        default: false
      },
      userLoading: {
        type: Boolean,
        default: false
      },
      assigneeList: {
        type: Array,
        default: () => ([])
      },
      backList: {
        type: Array,
        default: () => ([])
      },
      showAssign: {
        type: Boolean,
        default: false
      },
      modalTaskTitle: {
        type: String,
        default: ''
      },
      form: {
        type: Object,
        default: () => ({})
      }
    },
    data() {
      return {
        submitLoading: false, // 添加或编辑提交状态
        url: {
          pass: '/actTask/pass',
          back: '/actTask/back',
          backToTask: '/actTask/backToTask',
          delegate: '/actTask/delegate',
          getNode: '/activiti_process/getNode/'
        },
        error: '',
        assignees: []
      }
    },
    methods: {
      /* 审批人选中处理*/
      assignChangeHandle(index, selected) {
        this.assigneeList[index].values = selected
        console.log(this.assigneeList)
      },
      afterSub() {
        this.$emit('afterSub')
      },
      cancel() {
        this.$emit('cancel')
      },
      /*判断是否选择审批人*/
      notAssignee() {
        for (let item of this.assigneeList) {
          if (!item.values || item.values.length < 1) {
            this.$message.error(`${item.title}缺少审批人`)
            return true
          }
        }
      },
      /*审批提交的方法*/
      handelSubmit() {
        console.log('提交')
        this.submitLoading = true
        let formData = Object.assign({}, this.form)
        if (formData.type == 0) {
          // 通过
          if (this.showAssign && this.notAssignee()) {
            this.submitLoading = false
            return
          } else {
            this.error = ''
          }
          formData.assignees = []
          this.assigneeList.forEach(item => {
            if (item.values && item.values.length > 0) {
              let assignee = { id: item.id, assignees: item.values.join(',') }
              formData.assignees.push(assignee)
            }
          })
          postAction(this.url.pass, formData).then(res => {
            this.submitLoading = false
            if (res.success) {
              this.$message.success('操作成功')
              this.afterSub()
            }
          })
        } else if (formData.type == 1) {
          formData.assignees = formData.assignees.join(',')
          // 驳回
          if (formData.backTaskKey == '-1') {
            // 驳回至发起人
            postFormAction(this.url.back, formData).then(res => {
              this.submitLoading = false
              if (res.success) {
                this.$message.success('操作成功')
                this.afterSub()
              }
            })
          } else {
            // 自定义驳回
            if (formData.backTaskKey != '-1' && formData.assignees.length < 1) {
              this.$message.error('请至少选择一个审批人')
              this.submitLoading = false
              return
            } else {
              this.error = ''
            }
            postFormAction(this.url.backToTask, formData).then(res => {
              this.submitLoading = false
              if (res.success) {
                this.$message.success('操作成功')
                this.afterSub()
              }
            })
          }
        } else if (formData.type == 2) {
          // 委托
          if (!formData.userId) {
            this.$message.error('请选择一委托人')
            this.submitLoading = false
            return
          } else {
            this.error = ''
          }
          postFormAction(this.url.delegate, formData).then(res => {
            this.submitLoading = false
            if (res.success) {
              this.$message.success('操作成功')
              this.afterSub()
            }
          })
        }
      },
      changeBackTask(v) {
        if (v == '-1') {
          return
        }
        let params = getStore('lcModa')
        getAction(this.url.getNode, {
          nodeId: v,
          tableName: params.processData.tableName,
          tableId: params.processData.tableId
        }).then(res => {
          if (res.success) {
            if (res.result.users && res.result.users.length > 0) {
              this.assignees = res.result.users
              this.form.assignees = res.result.users.map(m => m.username)
            }
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>