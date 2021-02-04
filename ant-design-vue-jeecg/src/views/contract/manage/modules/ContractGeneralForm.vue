<template>
  <a-spin :spinning="confirmLoading">
    <a-card class="apply-card" title="合同信息">
      <j-form-container :disabled="disabled">
        <a-form-model slot="detail" ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="合同名称">
                <a-input v-model="form.name" placeholder="请输入合同名称"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="合同编号">
                <a-input v-model="form.code" placeholder="请输入合同编号"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="类型编码">
                <a-input v-model="form.typeCode" placeholder="请输入类型编码"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="备注">
                <a-input v-model="form.remark" placeholder="请输入备注"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="状态">
                <a-input v-model="form.status" placeholder="请输入状态"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="用户id">
                <a-input v-model="form.userId" placeholder="请输入用户id"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="签署方数">
                <a-input v-model="form.memberUse" placeholder="请输入签署方数"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="我方">
                <a-input v-model="form.firstMember" placeholder="请输入我方"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="乙方">
                <a-input v-model="form.secondMember" placeholder="请输入乙方"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="丙方">
                <a-input v-model="form.thirdMember" placeholder="请输入丙方"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="项目相关合同">
                <a-input-number v-model="form.subForm.relateProject" placeholder="请输入项目相关合同" style="width: 100%"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="相关项目">
                <a-input v-model="form.subForm.project" placeholder="请输入相关项目"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="是否子合同">
                <a-input-number v-model="form.subForm.isSub" placeholder="请输入是否子合同" style="width: 100%"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="主合同id">
                <a-input v-model="form.subForm.parentId" placeholder="请输入主合同id"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="预算相关合同">
                <a-input-number v-model="form.subForm.budget" placeholder="请输入预算相关合同" style="width: 100%"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="成本中心">
                <a-input v-model="form.subForm.costCenter" placeholder="请输入成本中心"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="密级">
                <a-input-number v-model="form.subForm.isSecret" placeholder="请输入密级" style="width: 100%"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="开始时间">
                <j-date placeholder="请选择开始时间" v-model="form.subForm.startTime" :trigger-change="true"
                        style="width: 100%"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="结束时间">
                <j-date placeholder="请选择结束时间" v-model="form.subForm.endTime" :trigger-change="true"
                        style="width: 100%"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="采购方式">
                <a-input-number v-model="form.subForm.purchaseType" placeholder="请输入采购方式" style="width: 100%"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="招标编号">
                <a-input v-model="form.subForm.biddingId" placeholder="请输入招标编号"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="相关框架协议">
                <a-input v-model="form.subForm.protocol" placeholder="请输入相关框架协议"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="采购订单号">
                <a-input v-model="form.subForm.purchaseId" placeholder="请输入采购订单号"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="合同金额">
                <a-input v-model="form.subForm.amount" placeholder="请输入合同金额"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="合同金额大写">
                <a-input v-model="form.subForm.amountLarge" placeholder="请输入合同金额大写"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="模板id">
                <a-input v-model="form.subForm.modelId" placeholder="请输入模板id"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="合同影像文件">
                <a-input v-model="form.subForm.filePdf" placeholder="请输入合同影像文件"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="附件">
                <a-input v-model="form.subForm.fileAttach" placeholder="请输入附件"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="合同文件">
                <a-input v-model="form.subForm.fileContract" placeholder="请输入合同文件"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </j-form-container>

    </a-card>

  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import { activitiApproveMixin } from '@views/activiti/mixins/activitiApproveMixin'

  export default {
    name: 'ContractGeneralForm',
    components: {
      JFormContainer,
      JDate
    },
    mixins: [activitiApproveMixin],
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data() {
      return {
        form: { subForm: {}, processData: {} },
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        validatorRules: {},
        url: {
          add: '/contract/contractGeneral/add',
          edit: '/contract/contractGeneral/edit',
          queryById: '/contract/contractGeneral/queryById'
        }
      }
    },
    computed: {},
    created() {

    },
    methods: {
      init() {
        this.getAction(this.url.queryById, { id: this.processData.tableId }).then((res) => {
          if (res.success) {
            this.form = res.result
            this.form.tableName = this.processData.tableName
            console.log('表单回显数据', this.form)
          } else {
            this.$message.error(res.message)
          }
        })
      },
      /*重写提交*/
      handleSubmit() {
        return new Promise(((resolve, reject) => {
          this.$refs.ruleForm.validate(valid => {
            if (valid) {
              this.form.processData.procDefId = this.processData.id
              this.form.processData.procDeTitle = this.title
              if (!this.form.processData.tableName) {
                this.form.processData.tableName = this.processData.businessTable
              }
              this.confirmLoading = true
              let httpurl = ''
              let method = ''
              if (!this.model.id) {
                httpurl += this.url.add
                method = 'post'
              } else {
                httpurl += this.url.edit
                method = 'put'
              }
              console.log('表单提交数据', this.form)
              httpAction(httpurl, this.form, method).then((res) => {
                if (res.success) {
                  this.$message.success(res.message)
                  resolve(this.form)
                } else {
                  this.$message.warning(res.message)
                }
              }).finally(() => {
                reject()
                this.confirmLoading = false
              })
              console.log('formData', this.form)
            } else {
              console.log('error submit!!')
              reject()
            }
          })
        }))
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'relateProject', 'project', 'isSub', 'parentId', 'budget', 'costCenter', 'isSecret', 'startTime', 'endTime', 'purchaseType', 'biddingId', 'protocol', 'purchaseId', 'amount', 'amountLarge', 'modelId', 'filePdf', 'fileAttach', 'fileContract'))
      }
    }
  }
</script>