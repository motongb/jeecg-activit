<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-card class="apply-card" title="基本信息">
            <a-col :span="8">
              <a-form-item label="名称1" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['nameCn']" placeholder="请输入中文名称"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="名称2" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['nameEn']" placeholder="请输入英文名称"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['code']" placeholder="请输入编码"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-decorator="['type',{initialValue:'1'}]" :triggerChange="true" placeholder="请输入类型"
                                   dictCode="company_type"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-decorator="['status',{initialValue:'3'}]" :triggerChange="true" placeholder="请输入状态"
                                   dictCode="company_status"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="存续状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-decorator="['liveStatus',{initialValue:'1'}]" :triggerChange="true"
                                   dictCode="company_live_status"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="组别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-decorator="['groups',{initialValue:'1000'}]" :triggerChange="true"
                                   dictCode="company_groups"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="属性" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-decorator="['attr',{initialValue:'0003'}]" :triggerChange="true"
                                   dictCode="company_attr"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="黑名单" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-radio-group v-decorator="['black',{initialValue:'0'}]">
                  <a-radio :value="0">否</a-radio>
                  <a-radio :value="1">是</a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-card>
          <a-card class="apply-card" title="详细信息">
            <a-col :span="8">
              <a-form-item label="国家" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag v-decorator="['country',{initialValue:'1'}]" :dictOptions="countryDictOptions"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="城市" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['city']" placeholder="请输入城市"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="区域" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-decorator="['region',{initialValue:'1'}]" :triggerChange="true"
                                   dictCode="country_region"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['address']" placeholder="请输入地址"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="服务范围" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-multi-select-tag v-decorator="['serviceRange']" dictCode="company_service_range"
                                    placeholder="请选择服务范围"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="行业" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-multi-select-tag v-decorator="['business']" dictCode="company_business"
                                    placeholder="请选择行业"/>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="简介" :labelCol="{ xs: { span: 24 },  sm: { span: 2 }}"
                           :wrapperCol="{ xs: { span: 24 }, sm: { span: 21 }}">
                <a-textarea v-decorator="['context']" placeholder="请输入简介" :rows="5"/>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="法人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['legal']" placeholder="请输入法人"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['contactPerson']" placeholder="请输入联系人"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['contactPhone']" placeholder="请输入联系电话"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="单位电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['link']" placeholder="请输入单位电话"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="传真" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['fax']" placeholder="请输入传真"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['email']" placeholder="请输入邮箱"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="邮编" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['postCode']" placeholder="请输入邮编"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="信用代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['creditCode']" placeholder="请输入信用代码"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="注册资本" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['capital']" placeholder="请输入注册资本"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="注册时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date v-decorator="['registerTime',{}]" placeholder="请输入注册时间" style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="省市县" :labelCol="{ xs: { span: 24 },  sm: { span: 4}}"
                           :wrapperCol="{ xs: { span: 24 }, sm: { span: 20 }}">
                <j-area-linkage v-decorator="['province']" type="select"></j-area-linkage>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="经营范围" :labelCol="{ xs: { span: 24 },  sm: { span: 2 }}"
                           :wrapperCol="{ xs: { span: 24 }, sm: { span: 21 }}">
                <a-textarea v-decorator="['register']" placeholder="请输入经营范围" :rows="5"/>
              </a-form-item>
            </a-col>
          </a-card>
          <a-card class="apply-card" title="银行信息">
            <company-bank-item ref="companyBankItem" :company-id="model.id"></company-bank-item>
          </a-card>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import { initDictOptions } from '@/components/dict/JDictSelectUtil'
  import CompanyBankItem from '../../components/CompanyBankItem'

  export default {
    name: 'CompanyForm',
    components: {
      CompanyBankItem,
      JFormContainer
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: () => {
        },
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data() {
      return {
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 15 }
        },
        confirmLoading: false,
        validatorRules: {},
        url: {
          add: '/contract/company/add',
          edit: '/contract/company/edit',
          queryById: '/contract/company/queryById'
        },
        countryDictOptions: []
      }
    },
    computed: {
      formDisabled() {
        if (this.formBpm === true) {
          if (this.formData.disabled === false) {
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton() {
        if (this.formBpm === true) {
          if (this.formData.disabled === false) {
            return true
          }
        }
        return false
      }
    },
    created() {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData()
      this.initDictConfig()
    },
    methods: {
      initDictConfig() {
        initDictOptions('country').then(res => {
          if (res.success) {
            this.countryDictOptions = res.result
          }
        })
      },
      add() {
        this.edit({})
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'nameCn', 'nameEn', 'code', 'level', 'type', 'status', 'black', 'serviceRange', 'business',
            'country', 'region', 'address', 'context', 'legal', 'register', 'creditCode', 'capital', 'registerTime', 'postCode',
            'liveStatus', 'link', 'email', 'fax', 'province', 'city', 'area', 'contactPerson', 'contactPhone'))
        })
      },
      //渲染流程表单数据
      showFlowData() {
        if (this.formBpm === true) {
          let params = { id: this.formData.dataId }
          getAction(this.url.queryById, params).then((res) => {
            if (res.success) {
              this.edit(res.result)
            }
          })
        }
      },
      submitForm() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
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
            let formData = Object.assign(this.model, values)
            formData.companyBanks = this.$refs.companyBankItem.getData()
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
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
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'nameCn', 'nameEn', 'code', 'level', 'type', 'status', 'black', 'serviceRange', 'business', 'country', 'region', 'address', 'context', 'legal', 'register', 'creditCode', 'capital', 'registerTime', 'liveStatus', 'link', 'email', 'fax', 'province', 'city', 'area', 'contactPerson', 'contactPhone'))
      }
    }
  }
</script>
<style scoped>
  .apply-card {
    margin-bottom: 24px;
  }
</style>