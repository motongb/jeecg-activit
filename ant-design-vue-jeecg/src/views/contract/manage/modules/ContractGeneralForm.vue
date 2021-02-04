<template>
  <a-spin :spinning="confirmLoading">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="表单">
        <j-form-container :disabled="disabled">
          <a-form-model slot="detail" ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-card class="apply-card" title="合同信息">
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
                  <a-form-model-item label="合同类型">
                    <a-tree-select v-model="form.typeCode" show-search
                                   treeNodeFilterProp="title"
                                   :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" placeholder="请选择合同类型"
                                   :tree-data="contractTypeData">
                    </a-tree-select>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12">
                  <a-form-model-item label="成本中心">
                    <a-input v-model="form.subForm.costCenter" placeholder="请输入成本中心"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24">
                  <a-form-model-item label="合同说明" :label-col="{ span: 2 }" :wrapper-col="{ span: 20 }">
                    <a-textarea v-model="form.remark" placeholder="请输入合同说明" :rows="5"/>
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
                  <a-form-model-item label="主合同">
                    <a-input v-model="form.subForm.parentId" placeholder="请输入主合同id"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12">
                  <a-form-model-item label="预算相关合同">
                    <a-input-number v-model="form.subForm.budget" placeholder="请输入预算相关合同" style="width: 100%"/>
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
                  <a-form-model-item label="合同金额">
                    <a-input v-model="form.subForm.amount" placeholder="请输入合同金额"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12">
                  <a-form-model-item label="合同金额大写">
                    <a-input v-model="form.subForm.amountLarge" placeholder="请输入合同金额大写"></a-input>
                  </a-form-model-item>
                </a-col>
              </a-row>
            </a-card>
            <a-card class="apply-card" title="采购方式">
              <a-row>
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
              </a-row>
            </a-card>
            <a-card class="apply-card" title="签署对象">
              <a-row>
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
              </a-row>
            </a-card>
            <a-card class="apply-card" title="签署文件">
              <a-row>
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
            </a-card>
            <!--            <a-card v-show="!task" class="apply-card" title="条件选择">-->
            <!--              <a-row>-->
            <!--                <a-col :span="24">-->
            <!--                  <a-form-model-item label="线路选择">-->
            <!--                    <a-radio-group v-model="form.params.condition">-->
            <!--                      <a-radio value="路线一">路线一</a-radio>-->
            <!--                      <a-radio value="路线二">路线二</a-radio>-->
            <!--                    </a-radio-group>-->
            <!--                  </a-form-model-item>-->
            <!--                </a-col>-->
            <!--              </a-row>-->
            <!--            </a-card>-->
          </a-form-model>
        </j-form-container>

      </a-tab-pane>
      <a-tab-pane key="2" tab="正文" force-render>
        Content of Tab Pane 2
      </a-tab-pane>
    </a-tabs>

  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { getStore } from '@/utils/storage'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import { activitiApproveMixin } from '@views/activiti/mixins/activitiApproveMixin'
  import ARow from 'ant-design-vue/es/grid/Row'

  export default {
    name: 'ContractGeneralForm',
    components: {
      ARow,
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
        // 合同类型树
        contractTypeData: [],
        // 表单
        form: { subForm: {}, processData: {}, params: {} },
        // 流程数据
        lcModa: {},
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
          add: '/contract/contractGeneral/add',
          edit: '/contract/contractGeneral/edit',
          queryById: '/contract/contractGeneral/queryById',
          treeList: '/contract/contractType/tree'
        }
      }
    },
    computed: {},
    created() {
      this.lcModa = getStore('lcModa')
      if (this.isNew) {
        this.form.typeCode = this.lcModa.typeCode
        this.form.processData.procDefId = this.processData.id
        this.form.processData.tableName = this.processData.businessTable
      }
      this.getContractType()
    },
    methods: {
      getContractType() {
        this.getAction(this.url.treeList, { roles: true }).then(res => {
          if (res.success) {
            let jsonStr = JSON.stringify(res.result)
            jsonStr = jsonStr.replace(/name/g, 'title').replace(/code/g, 'value')
            this.contractTypeData = JSON.parse(jsonStr)
            console.log(this.contractTypeData)
          }
        })
      },
      init() {
        this.getAction(this.url.queryById, { id: this.processData.tableId }).then((res) => {
          if (res.success) {
            this.form = res.result
            this.form.processData = {}
            console.log('表单回显数据', this.form)
          } else {
            this.$message.error(res.message)
          }
        })
      },
      /*重写提交*/
      handleSubmit() {
        return new Promise((resolve, reject) => {
          this.$refs.ruleForm.validate(valid => {
            if (valid) {
              this.form.processData.procDeTitle = this.title
              this.confirmLoading = true
              let httpurl = ''
              let method = ''
              if (this.isNew) {
                httpurl += this.url.add
                method = 'post'
              } else {
                httpurl += this.url.edit
                method = 'put'
              }
              console.log('表单提交数据', this.form)
              httpAction(httpurl, this.form, method).then(res => {
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
            } else {
              console.log('error submit!!')
              reject()
            }
          })
        })
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'relateProject', 'project', 'isSub', 'parentId', 'budget', 'costCenter', 'isSecret', 'startTime', 'endTime', 'purchaseType', 'biddingId', 'protocol', 'purchaseId', 'amount', 'amountLarge', 'modelId', 'filePdf', 'fileAttach', 'fileContract'))
      }
    }
  }
</script>
<style scoped>
  .apply-card {
    margin-bottom: 24px;
  }
</style>