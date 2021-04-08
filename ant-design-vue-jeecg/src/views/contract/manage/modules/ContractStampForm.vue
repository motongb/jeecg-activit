<template>
  <a-spin :spinning="confirmLoading">
    <a-form-model ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-card class="apply-card" title="合同信息">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="合同名称" :label-col="{ span: 2 }" :wrapper-col="{ span: 20 }">
              <p>{{record?record.name:''}}</p>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同编号">
              <p>{{record?record.code:''}}</p>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同类型">
              <p>{{record?record.typeName:''}}</p>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同金额">
              <p>{{record?record.amount.replace(/\B(?=(\d{3})+(?!\d))/g, ','):''}}</p>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同金额大写">
              <p>{{record?record.amountLarge:''}}</p>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-card>
      <a-card class="apply-card" title="签署对象">
        <a-row>
          <a-col :span="10">
            <a-row>
              <a-form-model-item label="签署方数">
                <p>{{record?(record.memberUse=='0'?'双方签署':'三方签署'):''}}</p>
              </a-form-model-item>
            </a-row>
            <a-row>
              <a-form-model-item label="我 方">
                <p>{{record?record.firstMemberObj.nameCn:''}}</p>
              </a-form-model-item>
            </a-row>
            <a-row>
              <a-form-model-item label="乙 方">
                <p>{{record?record.secondMemberObj.nameCn:''}}</p>
              </a-form-model-item>
            </a-row>
            <a-row v-if="record&&record.memberUse=='1'">
              <a-form-model-item label="丙 方">
                <p>{{record?record.thirdMemberObj.nameCn:''}}</p>
              </a-form-model-item>
            </a-row>
          </a-col>
          <a-col :span="14">
            <a-tabs style="margin-left: -95px" tab-position="left">
              <a-tab-pane key="0" tab="我 方">
                <contract-member-form v-if="record" :disabled="true" v-model="record.firstMemberObj"
                                      member-type="0"></contract-member-form>
              </a-tab-pane>
              <a-tab-pane key="1" tab="乙 方">
                <contract-member-form v-if="record" :disabled="true" v-model="record.secondMemberObj"
                                      member-type="1"></contract-member-form>
              </a-tab-pane>
              <a-tab-pane key="2" tab="丙 方" v-if="record&&record.memberUse=='1'">
                <contract-member-form v-if="record" :disabled="true" v-model="record.thirdMemberObj"
                                      member-type="2"></contract-member-form>
              </a-tab-pane>
            </a-tabs>
          </a-col>
        </a-row>
      </a-card>
      <a-card class="apply-card" title="用印信息">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="印章类型" :label-col="{ span: 2 }" :wrapper-col="{ span: 20 }">
              <a-checkbox-group :disabled="lcModa.disabled" v-model="selectedTypes">
                <a-row>
                  <template v-for="(item,index) in sealList">
                    <a-col :span="24">
                      <a-row>
                        <a-col :span="4">
                          <a-checkbox @change="e=>typesChange(e,index)" :value="index">
                            {{item.name}}
                          </a-checkbox>
                        </a-col>
                        <a-col :span="4">
                          <a-input size="small" :disabled="item.disabled" v-model="item.num"/>
                        </a-col>
                        <a-col :span="4"><i>份</i></a-col>
                      </a-row>
                    </a-col>
                  </template>
                </a-row>
              </a-checkbox-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :label-col="{ span: 2 }" :wrapper-col="{ span: 20 }">
              <a-textarea :disabled="lcModa.disabled" v-model="form.remark" placeholder="请输入备注" :rows="5"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="盖章文件">
              <j-upload :disabled="lcModa.disabled" :buttonVisible="!lcModa.disabled" v-model="form.fileIds"/>
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
          queryById: '/contract/contractStamp/queryById',
          getContractData: '/contract/contractStamp/getContractData',
          sealList: '/contract/contractSeal/list'
        },
        defaultObj: {},
        record: null,
        sealList: [],// 印章列表
        selectedTypes: []
      }
    },
    computed: {},
    async created() {
      if (this.lcModa.isNew) {
        //流程数据
        this.form.processData.title = this.lcModa.title
        this.form.processData.dept = this.lcModa.dept
        this.form.processData.tableName = this.lcModa.processData.tableName
        this.form.processData.procDefId = this.lcModa.processData.id
        // 表单数据
        if (this.lcModa.record) {
          this.record = this.lcModa.record
          this.form.record = this.record
          this.form.tableId = this.record.id
          this.form.tableName = this.record.tableName
          this.form.typeCode = this.record.typeCode
          this.form.contractName = this.record.name
          this.form.contractCode = this.record.code
        }
      } else {
        await this.init()
      }
      this.getSealList()
    },
    methods: {
      typesChange(e, index) {
        if (e.target.checked) {
          this.sealList[index].num = 1
        } else {
          this.sealList[index].num = undefined
        }
        this.sealList[index].disabled = !e.target.checked
      },
      /*获取印章数据*/
      getSealList() {
        getAction(this.url.sealList, { pageSize: 999 }).then(res => {
          this.sealList = res.result.records.map(m => {
            m.disabled = true
            return m
          })
          if (!this.lcModa.isNew) {
            this.handleStampType(this.form)
          }
        })
      },
      /*初始化数据*/
      init() {
        return new Promise((resolve, reject) => {
          getAction(this.url.queryById, { id: this.lcModa.processData.tableId }).then((res) => {
            if (res.success) {
              this.form = res.result
              this.record = res.result.record
              this.form.processData = {}
              resolve()
            } else {
              this.$message.error(res.message)
              reject()
            }
          }).catch(_ => reject())
        })
      },
      handleStampType(form) {
        if (form.stampType) {
          let stampTypes = form.stampType.split(',')
          let stampNums = form.stampNum.split(',')
          for (let i = 0; i < stampTypes.length; i++) {
            let index = this.sealList.findIndex(m => m.id === stampTypes[i])
            if (index < 0) {
              continue
            }
            this.sealList[index].checked = true
            this.sealList[index].num = stampNums[i]
            this.selectedTypes.push(index)
          }
        }
      },
      submitForm() {
        this.form.stampType = this.selectedTypes.map(i => this.sealList[i].id).join(',')
        this.form.stampNum = this.selectedTypes.map(i => this.sealList[i].num).join(',')
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