<template>
  <a-spin :spinning="confirmLoading">
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="表单">
        <a-form-model ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
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
                  <a-tree-select v-model="form.typeCode"
                                 :show-search="true"
                                 allow-clear
                                 treeNodeFilterProp="title"
                                 :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" placeholder="请选择合同类型"
                                 :replaceFields="{title:'name', key:'id', value: 'code' }"
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
                  <a-select v-model="form.subForm.relateProject">
                    <a-select-option value="0">项目无关合同</a-select-option>
                    <a-select-option value="1">项目相关合同</a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="相关项目">
                  <a-input-search v-model="form.subForm.project" :disabled="form.subForm.relateProject=='0'"
                                  placeholder="请选择相关项目"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="是否子合同">
                  <a-radio-group v-model="form.subForm.isSub">
                    <a-radio value="0">否</a-radio>
                    <a-radio value="1">是</a-radio>
                  </a-radio-group>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="主合同">
                  <a-input-search v-model="form.subForm.parentId" :disabled="form.subForm.isSub=='0'"
                                  placeholder="请选择主合同"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="预算相关合同">
                  <a-select v-model="form.subForm.budget">
                    <a-select-option value="0">预算内合同</a-select-option>
                    <a-select-option value="1">预售外合同</a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="密级">
                  <a-select v-model="form.subForm.isSecret">
                    <a-select-option value="0">非密</a-select-option>
                    <a-select-option value="1">保密</a-select-option>
                  </a-select>
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
                  <a-input-number v-model="form.subForm.amount" style="width: 100%"
                                  :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                  :parser="value => value.replace(/\$\s?|(,*)/g, '')"
                                  @change="amountChange"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="合同金额大写">
                  <a-input v-model="form.subForm.amountLarge" disabled></a-input>
                </a-form-model-item>
              </a-col>
            </a-row>
          </a-card>
          <a-card class="apply-card" title="采购方式">
            <a-row>
              <a-col :span="12">
                <a-form-model-item label="采购方式">
                  <j-dict-select-tag v-model="form.subForm.purchaseType" dictCode="purchase_way"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="招标编号">
                  <a-input-search v-model="form.subForm.biddingId" placeholder="请选择招标编号"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="相关框架协议">
                  <a-input-search v-model="form.subForm.protocol" placeholder="请选择相关框架协议"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="采购订单号">
                  <a-input-search v-model="form.subForm.purchaseId" placeholder="请选择采购订单"/>
                </a-form-model-item>
              </a-col>
            </a-row>
          </a-card>
          <a-card class="apply-card" title="合同明细项">
            <contract-item :disabled="disabled" ref="contractItem" :contract-id="processData.tableId"
                           :is-new="isNew"></contract-item>
          </a-card>
          <a-card class="apply-card" title="付款约定">
            <contract-payment :disabled="disabled" ref="contractPayment" :contract-id="processData.tableId"
                              :is-new="isNew"></contract-payment>
          </a-card>
          <a-card class="apply-card" title="签署对象">
            <a-row>
              <a-col :span="10">
                <a-row>
                  <a-form-model-item label="签署方数">
                    <a-select v-model="form.memberUse">
                      <a-select-option value="0">双方签署</a-select-option>
                      <a-select-option value="1">三方签署</a-select-option>
                    </a-select>
                  </a-form-model-item>
                </a-row>
                <a-row>
                  <a-form-model-item label="我 方">
                    <CompanySelectTag v-model="form.firstMember"
                                      @select="(item)=>handleCompanySelect(item,'0')"></CompanySelectTag>
                  </a-form-model-item>
                </a-row>
                <a-row>
                  <a-form-model-item label="乙 方">
                    <CompanySelectTag v-model="form.secondMember"
                                      @select="(item)=>handleCompanySelect(item,'1')"></CompanySelectTag>
                  </a-form-model-item>
                </a-row>
                <a-row v-if="form.memberUse=='1'">
                  <a-form-model-item label="丙 方">
                    <CompanySelectTag v-model="form.thirdMember"
                                      @select="(item)=>handleCompanySelect(item,'2')"></CompanySelectTag>
                  </a-form-model-item>
                </a-row>
              </a-col>
              <a-col :span="14">
                <a-tabs style="margin-left: -95px" tab-position="left">
                  <a-tab-pane key="0" tab="我 方">
                    <contract-member-form :disabled="disabled" v-model="form.firstMemberObj"
                                          member-type="0"></contract-member-form>
                  </a-tab-pane>
                  <a-tab-pane key="1" tab="乙 方">
                    <contract-member-form :disabled="disabled" v-model="form.secondMemberObj"
                                          member-type="1"></contract-member-form>
                  </a-tab-pane>
                  <a-tab-pane key="2" tab="丙 方" v-if="form.memberUse=='1'">
                    <contract-member-form :disabled="disabled" v-model="form.thirdMemberObj"
                                          member-type="2"></contract-member-form>
                  </a-tab-pane>
                </a-tabs>
              </a-col>
            </a-row>
          </a-card>
          <a-card class="apply-card" title="签署文件">
            <a-row>
              <a-col :span="12">
                <a-form-model-item label="模板id">
                  <a-input v-model="form.subForm.modelId" placeholder="请输入模板id" @click.native="showFileUpload"></a-input>
                  <j-file-pop ref="filePop" @ok="handleFileSuccess"></j-file-pop>
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
      </a-tab-pane>
      <a-tab-pane key="2" tab="正文" force-render>
        Content of Tab Pane 2
      </a-tab-pane>
    </a-tabs>

  </a-spin>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import { validateDuplicateValue, digitUppercase } from '@/utils/util'
  import { getStore } from '@/utils/storage'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import { activitiApproveMixin } from '@views/activiti/mixins/activitiApproveMixin'
  import ARow from 'ant-design-vue/es/grid/Row'
  import moment from 'moment'
  import pick from 'lodash.pick'
  import ContractItem from '../../components/ContractItem'
  import ContractPayment from '../../components/ContractPayment'
  import ContractMemberForm from './ContractMemberForm'
  import CompanySelectTag from '../../components/CompanySelectTag'

  export default {
    name: 'ContractGeneralForm',
    components: {
      CompanySelectTag,
      ContractMemberForm,
      ContractPayment,
      ContractItem,
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
        form: {
          typeCode: undefined,
          memberUse: '0',
          subForm: {
            amount: 0,
            amountLarge: '零元整',
            relateProject: '0',
            isSub: '0',
            budget: '0',
            isSecret: '0',
            purchaseType: '0',
            startTime: moment().format('YYYY-MM-DD'),
            endTime: moment().subtract(-1, 'years').format('YYYY-MM-DD')
          },
          firstMemberObj: { type: '0', coin: 'CNY' },
          secondMemberObj: { type: '1', coin: 'CNY' },
          thirdMemberObj: { type: '2', coin: 'CNY' },
          processData: {},
          params: {}
        },
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
          treeList: '/contract/contractType/tree',
          bankList: '/contract/companyBank/list'
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
        this.form.processData.dept = this.lcModa.dept
        this.form.name = this.lcModa.dept + '-' + this.lcModa.typeName
        this.form.code = this.lcModa.dept + '-' + this.lcModa.typeName + moment().format('YYYYMMDD')
      }
      this.getContractType()
    },
    methods: {
      showFileUpload(){
        this.$refs.filePop.show('','')
      },
      /*文件选择回调*/
      handleFileSuccess(obj) {
        console.log(obj)
      },
      /*企业选择回调*/
      handleCompanySelect(item, type) {
        let memberObj = {}
        memberObj = pick(item, 'nameCn', 'nameEn', 'legal', 'contactPerson', 'contactPhone', 'address', 'creditCode')
        memberObj = Object.assign({
          type: type,
          coin: 'CNY',
          amount: 0,
          payAmount: 0,
          lockAmount: 0,
          restAmount: 0
        }, memberObj)
        this.getAction(this.url.bankList, { companyId: item.id }).then(res => {
          if (res.success && res.result.records.length > 0) {
            let bankItem = res.result.records[0]
            memberObj.bank = bankItem.bank
            memberObj.bankNo = bankItem.bankNo
            memberObj.bankName = bankItem.bankName
          }
          if (type === '0') {
            this.form.firstMemberObj = memberObj
          } else if (type === '1') {
            this.form.secondMemberObj = memberObj
          } else if (type === '2') {
            this.form.thirdMemberObj = memberObj
          }
        })
      },
      /*金额变动*/
      amountChange(value) {
        this.form.subForm.amountLarge = digitUppercase(value)
      },
      /*获取合同类型树*/
      getContractType() {
        this.getAction(this.url.treeList, { roles: true }).then(res => {
          if (res.success) {
            this.contractTypeData = res.result
          }
        })
      },
      /*初始化数据*/
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
              this.form.contractItems = this.$refs.contractItem.getData()
              this.form.contractPayments = this.$refs.contractPayment.getData()
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
      }
    }
  }
</script>
<style scoped>
  .apply-card {
    margin-bottom: 24px;
  }
</style>