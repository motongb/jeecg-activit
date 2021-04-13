<template>
  <a-spin :spinning="confirmLoading">
    <a-tabs :active-key="activeKey" @change="tabChange">
      <a-tab-pane key="1" tab="表单">
        <j-form-container :disabled="formDisabled">
          <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
            <a-card class="apply-card" title="合同信息">
              <a-row>
                <a-col :span="8">
                  <a-form-model-item label="合同名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
                    <a-input v-model="model.name" placeholder="请输入合同名称"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="合同编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
                    <a-input v-model="model.code" placeholder="请输入合同编号"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="合同类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="typeCode">
                    <a-tree-select v-model="model.typeCode"
                                   :show-search="true"
                                   allow-clear
                                   treeNodeFilterProp="title"
                                   :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" placeholder="请选择合同类型"
                                   :replaceFields="{title:'name', key:'id', value: 'code' }"
                                   :tree-data="contractTypeData">
                    </a-tree-select>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="经办人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userId">
                    <a-input v-model="model.userId" placeholder="请输入经办人"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="自动编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isAutoCode">
                    <a-radio-group default-value="0" v-model="model.isAutoCode">
                      <a-radio value="0">否</a-radio>
                      <a-radio value="1">是</a-radio>
                    </a-radio-group>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="单位属性" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyCode">
                    <a-input v-model="model.companyCode" placeholder="请输入公司代码"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="采购方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="purchaseWay">
                    <j-dict-select-tag v-model="model.purchaseWay" placeholder="请选择采购方式" dictCode="purchase_way"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="programNo">
                    <a-input v-model="model.programNo" placeholder="请输入项目编号"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="programName">
                    <a-input v-model="model.programName" placeholder="请输入项目名称"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="招标编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="postCode">
                    <a-input v-model="model.postCode" placeholder="请输入招标编号"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="中标id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="biddingId">
                    <a-input v-model="model.biddingId" placeholder="请输入中标id"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="合同性质" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="attr">
                    <j-dict-select-tag v-model="model.attr" placeholder="请选择合同性质" dictCode="contract_attract"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="年度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="year">
                    <a-input v-model="model.year" placeholder="请输入年度"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="startTime">
                    <j-date placeholder="请选择开始时间" v-model="model.startTime" style="width: 100%"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endTime">
                    <j-date placeholder="请选择结束时间" v-model="model.endTime" style="width: 100%"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="合同价款" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amount">
                    <a-input v-model="model.amount" placeholder="请输入合同价款"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="让利" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jangli">
                    <a-input v-model="model.jangli" placeholder="请输入让利"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="币种" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="coin">
                    <j-dict-select-tag v-model="model.coin" placeholder="请选择货币码" dictCode="coin_type"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="付款方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payWay">
                    <j-dict-select-tag v-model="model.payWay" placeholder="请选择付款方式" dictCode="contract_pay_way"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="是否含税" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isRate">
                    <a-radio-group v-model="model.isRate">
                      <a-radio value="0">否</a-radio>
                      <a-radio value="1">是</a-radio>
                    </a-radio-group>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8" v-show="model.isRate=='1'">
                  <a-form-model-item label="税率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rate">
                    <a-input v-model="model.rate" placeholder="请输入税率"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="是否安装" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isInstall">
                    <a-radio-group default-value="0" v-model="model.isInstall">
                      <a-radio value="0">否</a-radio>
                      <a-radio value="1">是</a-radio>
                    </a-radio-group>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="签订地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signPlace">
                    <a-input v-model="model.signPlace" placeholder="请输入签订地点"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="签订时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signTime">
                    <j-date placeholder="请选择签订时间" v-model="model.signTime" style="width: 100%"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="link">
                    <a-input v-model="model.link" placeholder="请输入联系方式"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24">
                  <a-form-model-item label="备注" :label-col="{ span: 2 }" :wrapper-col="{ span: 21 }" prop="remark">
                    <a-textarea v-model="model.remark" placeholder="请输入备注" :rows="5"/>
                  </a-form-model-item>
                </a-col>
              </a-row>
            </a-card>
            <a-card class="apply-card" title="签署文件">
              <a-row>
                <a-col :span="24">
                  <a-form-model-item label="有无技术协议" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }"
                                     prop="hasProtocol">
                    <a-radio-group v-model="model.hasProtocol">
                      <a-radio value="0">无</a-radio>
                      <a-radio value="1">有</a-radio>
                    </a-radio-group>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-show="model.hasProtocol=='1'">
                  <a-form-model-item label="技术协议" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }" prop="protocol">
                    <j-upload :buttonVisible="!formDisabled" v-model="model.protocol"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24">
                  <a-form-model-item label="合同附件" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }"
                                     prop="fileAttach">
                    <j-upload :buttonVisible="!formDisabled" v-model="model.fileAttach"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24">
                  <a-form-model-item label="使用模板" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }" prop="useModel">
                    <a-radio-group v-model="model.useModel">
                      <a-radio value="0">否</a-radio>
                      <a-radio value="1">是</a-radio>
                    </a-radio-group>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12">
                  <a-form-model-item label="原模板文件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sourceModel">
                    <model-select :disabled="model.useModel=='0'" v-model="model.sourceModel"
                                  @change="modelSelectChange"></model-select>
                  </a-form-model-item>
                </a-col>
              </a-row>
            </a-card>
            <a-card class="apply-card" title="中标详情"></a-card>
            <a-card class="apply-card" title="签订对象">
              <a-row>
                <a-col :span="8">
                  <a-form-model-item label="签署方数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="memberUse">
                    <a-input-number v-model="model.memberUse" placeholder="请输入签署方数" style="width: 100%"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="我方编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="firstMember">
                    <a-input v-model="model.firstMember" placeholder="请输入我方编码"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="乙方编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="secondMember">
                    <a-input v-model="model.secondMember" placeholder="请输入乙方编码"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="8">
                  <a-form-model-item label="丙方编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thirdMember">
                    <a-input v-model="model.thirdMember" placeholder="请输入丙方编码"></a-input>
                  </a-form-model-item>
                </a-col>
              </a-row>
            </a-card>
          </a-form-model>
        </j-form-container>
      </a-tab-pane>
      <a-tab-pane key="2" tab="正文">sss</a-tab-pane>
    </a-tabs>

  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import ModelSelect from '../../components/ModelSelect'

  export default {
    name: 'LgContractPurchaseForm',
    components: { ModelSelect },
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
        model: {
          useModel: '0',
          hasProtocol: '0',
          isRate: '0'
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        validatorRules: {},
        url: {
          add: '/contract/lgContractPurchase/add',
          edit: '/contract/lgContractPurchase/edit',
          queryById: '/contract/lgContractPurchase/queryById',
          treeList: '/contract/contractType/tree'
        },
        activeKey: '1',
        contractTypeData: []
      }
    },
    computed: {
      formDisabled() {
        return this.disabled
      }
    },
    created() {
      //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model))
      this.getContractType()
    },
    methods: {
      modelSelectChange() {

      },
      tabChange(key) {
        this.activeKey = key
      },
      /*获取合同类型树*/
      getContractType() {
        getAction(this.url.treeList, { roles: true }).then(res => {
          if (res.success) {
            this.contractTypeData = res.result
          }
        })
      },
      add() {
        this.edit(this.modelDefault)
      },
      edit(record) {
        this.model = Object.assign({}, record)
        this.visible = true
      },
      submitForm() {
        const that = this
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            httpAction(httpurl, this.model, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
            })
          }

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