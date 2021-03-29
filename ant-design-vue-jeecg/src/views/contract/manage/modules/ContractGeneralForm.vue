<template>
  <a-spin :spinning="confirmLoading">
    <a-tabs :active-key="activeKey" @change="tabChange">
      <a-tab-pane key="1" tab="表单">
        <a-form-model ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
          <a-card class="apply-card" title="合同信息">
            <a-row>
              <a-col :span="12">
                <a-form-model-item label="合同名称">
                  <a-input :disabled="lcModa.disabled" v-model="form.name" placeholder="请输入合同名称"></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="合同编号">
                  <a-input :disabled="lcModa.disabled" v-model="form.code" placeholder="请输入合同编号"></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="合同类型">
                  <a-tree-select :disabled="lcModa.disabled" v-model="form.typeCode"
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
                  <a-input :disabled="lcModa.disabled" v-model="form.costCenter" placeholder="请输入成本中心"></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="24">
                <a-form-model-item label="合同说明" :label-col="{ span: 2 }" :wrapper-col="{ span: 20 }">
                  <a-textarea :disabled="lcModa.disabled" v-model="form.remark" placeholder="请输入合同说明" :rows="5"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="项目相关合同">
                  <a-select :disabled="lcModa.disabled" v-model="form.relateProject">
                    <a-select-option value="0">项目无关合同</a-select-option>
                    <a-select-option value="1">项目相关合同</a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="相关项目">
                  <a-input-search v-model="form.project" :disabled="form.relateProject=='0'||lcModa.disabled"
                                  placeholder="请选择相关项目"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="是否子合同">
                  <a-radio-group :disabled="lcModa.disabled" v-model="form.isSub">
                    <a-radio value="0">否</a-radio>
                    <a-radio value="1">是</a-radio>
                  </a-radio-group>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="主合同">
                  <a-input-search v-model="form.parentId" :disabled="form.isSub=='0'||lcModa.disabled"
                                  placeholder="请选择主合同"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="预算相关合同">
                  <a-select :disabled="lcModa.disabled" v-model="form.budget">
                    <a-select-option value="0">预算内合同</a-select-option>
                    <a-select-option value="1">预售外合同</a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="密级">
                  <a-select :disabled="lcModa.disabled" v-model="form.isSecret">
                    <a-select-option value="0">非密</a-select-option>
                    <a-select-option value="1">保密</a-select-option>
                  </a-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="开始时间">
                  <j-date :disabled="lcModa.disabled" placeholder="请选择开始时间" v-model="form.startTime"
                          :trigger-change="true"
                          style="width: 100%"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="结束时间">
                  <j-date :disabled="lcModa.disabled" placeholder="请选择结束时间" v-model="form.endTime"
                          :trigger-change="true"
                          style="width: 100%"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="合同金额">
                  <a-input-number :disabled="lcModa.disabled" v-model="form.amount" style="width: 100%"
                                  :formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                                  :parser="value => value.replace(/\$\s?|(,*)/g, '')"
                                  @change="amountChange"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="合同金额大写">
                  <p>{{form.amountLarge}}</p>
                </a-form-model-item>
              </a-col>
            </a-row>
          </a-card>
          <a-card class="apply-card" title="采购方式">
            <a-row>
              <a-col :span="12">
                <a-form-model-item label="采购方式">
                  <j-dict-select-tag :disabled="lcModa.disabled" v-model="form.purchaseType" dictCode="purchase_way"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="招标编号">
                  <a-input-search :disabled="lcModa.disabled" v-model="form.biddingId" placeholder="请选择招标编号"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="相关框架协议">
                  <a-input-search :disabled="lcModa.disabled" v-model="form.protocol" placeholder="请选择相关框架协议"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="采购订单号">
                  <a-input-search :disabled="lcModa.disabled" v-model="form.purchaseId" placeholder="请选择采购订单"/>
                </a-form-model-item>
              </a-col>
            </a-row>
          </a-card>
          <a-card class="apply-card" title="合同明细项">
            <contract-item :disabled="lcModa.disabled" ref="contractItem" :contract-id="lcModa.processData.tableId"
                           :is-new="lcModa.isNew"></contract-item>
          </a-card>
          <a-card class="apply-card" title="付款约定">
            <contract-payment :disabled="lcModa.disabled" ref="contractPayment"
                              :contract-id="lcModa.processData.tableId"
                              :is-new="lcModa.isNew"></contract-payment>
          </a-card>
          <a-card class="apply-card" title="签署对象">
            <a-row>
              <a-col :span="10">
                <a-row>
                  <a-form-model-item label="签署方数">
                    <a-select :disabled="lcModa.disabled" v-model="form.memberUse">
                      <a-select-option value="0">双方签署</a-select-option>
                      <a-select-option value="1">三方签署</a-select-option>
                    </a-select>
                  </a-form-model-item>
                </a-row>
                <a-row>
                  <a-form-model-item label="我 方">
                    <CompanySelectTag :disabled="lcModa.disabled" v-model="form.firstMember"
                                      @select="(item)=>handleCompanySelect(item,'0')"></CompanySelectTag>
                  </a-form-model-item>
                </a-row>
                <a-row>
                  <a-form-model-item label="乙 方">
                    <CompanySelectTag :disabled="lcModa.disabled" v-model="form.secondMember"
                                      @select="(item)=>handleCompanySelect(item,'1')"></CompanySelectTag>
                  </a-form-model-item>
                </a-row>
                <a-row v-if="form.memberUse=='1'">
                  <a-form-model-item label="丙 方">
                    <CompanySelectTag :disabled="lcModa.disabled" v-model="form.thirdMember"
                                      @select="(item)=>handleCompanySelect(item,'2')"></CompanySelectTag>
                  </a-form-model-item>
                </a-row>
              </a-col>
              <a-col :span="14">
                <a-tabs style="margin-left: -95px" tab-position="left">
                  <a-tab-pane key="0" tab="我 方">
                    <contract-member-form :disabled="lcModa.disabled" v-model="form.firstMemberObj"
                                          member-type="0"></contract-member-form>
                  </a-tab-pane>
                  <a-tab-pane key="1" tab="乙 方">
                    <contract-member-form :disabled="lcModa.disabled" v-model="form.secondMemberObj"
                                          member-type="1"></contract-member-form>
                  </a-tab-pane>
                  <a-tab-pane key="2" tab="丙 方" v-if="form.memberUse=='1'">
                    <contract-member-form :disabled="lcModa.disabled" v-model="form.thirdMemberObj"
                                          member-type="2"></contract-member-form>
                  </a-tab-pane>
                </a-tabs>
              </a-col>
            </a-row>
          </a-card>
          <a-card class="apply-card" title="签署文件">
            <a-row>
              <a-col :span="12">
                <a-form-model-item label="是否使用模板">
                  <a-radio-group :disabled="lcModa.disabled" v-model="form.useModel">
                    <a-radio value="0">否</a-radio>
                    <a-radio value="1">是</a-radio>
                  </a-radio-group>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item v-show="form.useModel==='1'" label="模板文件">
                  <model-select :disabled="lcModa.disabled" v-model="form.sourceModel"
                                @change="modelSelectChange"></model-select>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="合同影像文件">
                  <j-upload :disabled="lcModa.disabled" :buttonVisible="!lcModa.disabled" v-model="form.filePdf"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="附件">
                  <j-upload :disabled="lcModa.disabled" :buttonVisible="!lcModa.disabled" v-model="form.fileAttach"/>
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
      <a-tab-pane key="2" tab="正文" :forceRender="true">
        <wps-view-tag ref="wpsView" @fileOpen="fileOpenCallback" :is-model="isModel" :permission="permission"
                      :show-open-doc-btn="form.useModel==='0'"></wps-view-tag>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { digitUppercase } from '@/utils/util'
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
  import WpsViewTag from '../../components/WpsViewTag'
  import ModelSelect from '../../components/ModelSelect'
  import { copyByModelFile } from '@/api/wpsApi'

  export default {
    name: 'ContractGeneralForm',
    components: {
      ModelSelect,
      WpsViewTag,
      CompanySelectTag,
      ContractMemberForm,
      ContractPayment,
      ContractItem,
      ARow,
      JFormContainer,
      JDate
    },
    mixins: [activitiApproveMixin],
    props: {},
    data() {
      return {
        // 合同类型树
        contractTypeData: [],
        // 表单
        form: {
          sourceModel: '',
          fileContract: '',
          fileModel: '',
          useModel: '0',
          typeCode: undefined,
          memberUse: '0',
          amount: 0,
          amountLarge: '零元整',
          relateProject: '0',
          isSub: '0',
          budget: '0',
          isSecret: '0',
          purchaseType: '0',
          startTime: moment().format('YYYY-MM-DD'),
          endTime: moment().subtract(-1, 'years').format('YYYY-MM-DD'),
          filePdf: '',
          fileAttach: '',
          firstMemberObj: { type: '0', coin: 'CNY' },
          secondMemberObj: { type: '1', coin: 'CNY' },
          thirdMemberObj: { type: '2', coin: 'CNY' },
          processData: {},
          params: {}
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
          add: '/contract/contractPurchase/add',
          edit: '/contract/contractPurchase/edit',
          queryById: '/contract/contractPurchase/queryById',
          treeList: '/contract/contractType/tree',
          bankList: '/contract/companyBank/list'
        },
        docType: 'word',
        activeKey: '1',
        isModel: false, //是否模板
        permission: 'write'
      }
    },
    computed: {},
    watch: {
      tableId() {
        this.init()
      }
    },
    async created() {
      if (this.lcModa.isNew) {
        if (this.lcModa.isCopy) {
          this.tableId = this.lcModa.processData.tableId
          await this.init()
          this.form = this.clearField(this.form)
          this.form.processData.procDefId = this.lcModa.processData.procDefId
          // 文档不复制
          this.form.sourceModel = undefined
          this.form.fileContract = undefined
          this.form.fileModel = undefined
        } else {
          this.form.processData.procDefId = this.lcModa.processData.id
          this.form.typeCode = this.lcModa.typeCode
        }
        this.form.processData.title = this.lcModa.title
        this.form.processData.dept = this.lcModa.dept
        this.form.processData.tableName = this.lcModa.processData.tableName
        console.log(this.form)
      }
      this.getContractType()
      this.docIsRead()
    },
    methods: {
      /* 文档可编辑*/
      docIsRead() {
        this.permission = this.lcModa.disabled ? 'read' : 'write'
      },
      /*模板选择回调*/
      modelSelectChange() {
        if (this.form.useModel === '1' &&
          this.form.sourceModel &&
          this.form.sourceModel.length > 0) {
          copyByModelFile(this.form.sourceModel).then(res => {
            this.form.fileModel = res.result.fileId
          })
        }
      },
      /*文件打开回调*/
      fileOpenCallback(file) {
        if (file) {
          this.form.fileContract = file.id.split('-')[1]
        }
        console.log(this.form.fileContract)
      },
      tabChange(key) {
        this.activeKey = key
        if (key === '1') {
          this.$refs.wpsView.destroyIframe()
        } else if (this.form.useModel === '0' || this.lcModa.isTask) {//打开正文
          this.isModel = false
          this.$refs.wpsView.init(this.form.fileContract)
        } else if (this.form.useModel === '1' &&
          this.form.sourceModel &&
          this.form.sourceModel.length > 0) { // 打开模板
          this.isModel = true
          this.$refs.wpsView.init(this.form.fileModel)
        }
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
        getAction(this.url.bankList, { companyId: item.id }).then(res => {
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
        this.form.amountLarge = digitUppercase(value)
      },
      /*获取合同类型树*/
      getContractType() {
        getAction(this.url.treeList, { roles: true }).then(res => {
          if (res.success) {
            this.contractTypeData = res.result
          }
        })
      },
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
      clearField(item) {
        item.id = undefined
        item.createTime = undefined
        item.updateBy = undefined
        item.updateTime = undefined
        return item
      },
      /*重写提交*/
      handleSubmit() {
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
              this.form.contractItems = this.$refs.contractItem.getData().map(item => this.clearField(item))
              this.form.contractPayments = this.$refs.contractPayment.getData().map(item => this.clearField(item))
              this.form.firstMemberObj = this.clearField(this.form.firstMemberObj)
              this.form.secondMemberObj = this.clearField(this.form.secondMemberObj)
              this.form.thirdMemberObj = this.clearField(this.form.thirdMemberObj)
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