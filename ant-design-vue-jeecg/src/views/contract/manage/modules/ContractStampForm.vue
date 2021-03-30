<template>
  <a-spin :spinning="confirmLoading">
    <a-form-model ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-card class="apply-card" title="合同信息">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="合同名称" :label-col="{ span: 2 }" :wrapper-col="{ span: 20 }">
              <p>{{form.record?form.record.name:''}}</p>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同编号">
              <p>{{form.record?form.record.code:''}}</p>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同类型">
              <p>{{form.record?form.record.typeCode:''}}</p>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同金额">
              <p>{{form.record?form.record.amount:''}}</p>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同金额大写">
              <p>{{form.record?form.record.amountLarge:''}}</p>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-card>
      <a-card class="apply-card" title="签署对象">
        <a-row>
          <a-col :span="10">
            <a-row>
              <a-form-model-item label="签署方数">
                <p>{{form.record?(form.record.memberUse=='0'?'双方签署':'三方签署'):''}}</p>
              </a-form-model-item>
            </a-row>
            <a-row>
              <a-form-model-item label="我 方">
                <p>{{form.record?form.record.firstMemberName:''}}</p>
              </a-form-model-item>
            </a-row>
            <a-row>
              <a-form-model-item label="乙 方">
                <p>{{form.record?form.record.secondMemberName:''}}</p>
              </a-form-model-item>
            </a-row>
            <a-row v-if="form.record&&form.record.memberUse=='1'">
              <a-form-model-item label="丙 方">
                <p>{{form.record?form.record.thirdMemberName:''}}</p>
              </a-form-model-item>
            </a-row>
          </a-col>
          <a-col :span="14">
            <a-tabs style="margin-left: -95px" tab-position="left">
              <a-tab-pane key="0" tab="我 方">
                <contract-member-form v-if="form.record" :disabled="true" v-model="form.record.firstMemberObj"
                                      member-type="0"></contract-member-form>
              </a-tab-pane>
              <a-tab-pane key="1" tab="乙 方">
                <contract-member-form v-if="form.record" :disabled="true" v-model="form.record.secondMemberObj"
                                      member-type="1"></contract-member-form>
              </a-tab-pane>
              <a-tab-pane key="2" tab="丙 方" v-if="form.record&&form.record.memberUse=='1'">
                <contract-member-form v-if="form.record" :disabled="true" v-model="form.record.thirdMemberObj"
                                      member-type="2"></contract-member-form>
              </a-tab-pane>
            </a-tabs>
          </a-col>
        </a-row>
      </a-card>
      <a-card class="apply-card" title="用印信息">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="印章类型">
              <a-input style="width: 100%" v-model="form.stampType" placeholder="请输入印章类型"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="印章份数">
              <a-input v-model="form.stampNum" placeholder="请输入印章份数"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :label-col="{ span: 2 }" :wrapper-col="{ span: 20 }">
              <a-textarea v-model="form.remark" placeholder="请输入备注" :rows="5"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="盖章文件">
              <a-input v-model="form.fileIds" placeholder="请输入盖章文件"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-card>
    </a-form-model>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import ContractMemberForm from './ContractMemberForm'

  export default {
    name: 'ContractStampForm',
    components: { ContractMemberForm },
    props: {
      lcModa: {
        type: Object,
        default: () => ({
          dept: '', //部门
          title: '',//标题
          disabled: false,// 全局禁用
          processData: {},//流程数据
          isNew: false,//是否新增
          isTask: false//是否处理流程
        })
      }
    },
    data() {
      return {
        form: {
          processData: {}
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        validatorRules: {},
        url: {
          add: '/contract/contractStamp/add',
          edit: '/contract/contractStamp/edit',
          queryById: '/contract/contractStamp/queryById'
        },
        defaultObj: {}
      }
    },
    computed: {},
    created() {
      if (this.lcModa.isNew) {
        //流程数据
        this.form.processData.title = this.lcModa.title
        this.form.processData.dept = this.lcModa.dept
        this.form.processData.tableName = this.lcModa.processData.tableName
        this.form.processData.procDefId = this.lcModa.processData.id
        // 表单数据
        if (this.lcModa.record) {
          this.form.tableId = this.lcModa.record.id
          this.form.tableName = this.lcModa.record.tableName
          this.form.typeCode = this.lcModa.record.typeCode
          this.form.contractName = this.lcModa.record.name
          this.form.contractCode = this.lcModa.record.code
          this.form.record = this.lcModa.record
        }
      } else {
        this.init()
      }
    },
    methods: {
      /*初始化数据*/
      init() {
        return new Promise((resolve, reject) => {
          getAction(this.url.queryById, { id: this.lcModa.processData.tableId }).then((res) => {
            if (res.success) {
              this.form = res.result
              this.form.processData = {}
              resolve()
            } else {
              this.$message.error(res.message)
              reject()
            }
          }).catch(_ => reject())
        })
      },
      submitForm() {
        return new Promise((resolve, reject) => {
          this.$refs.ruleForm.validate(valid => {
            if (valid) {
              this.confirmLoading = true
              let httpurl = ''
              let method = ''
              if (this.lcModa.isNew) {
                httpurl += this.url.add
                method = 'post'
              } else {
                httpurl += this.url.edit
                method = 'put'
              }
              httpAction(httpurl, this.form, method).then((res) => {
                if (res.success) {
                  this.$message.success(res.message)
                  resolve(this.form)
                } else {
                  this.$message.warning(res.message)
                  reject()
                }
              }).finally(() => {
                this.confirmLoading = false
                reject()
              })
            }
          })
        })
      }
    }
  }
</script>
<style scoped>
  .apply-card {
    margin-top: 24px;
  }
</style>