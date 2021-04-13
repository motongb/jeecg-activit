<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="disabled">
      <a-form-model slot="detail" ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="名称" :labelCol="{  xs: { span: 24 }, sm: { span: 2 } }"
                               :wrapperCol="{ xs: { span: 24 }, sm: { span: 21 }}">
              <a-input v-model="form.nameCn" placeholder="请输入名称" @change="onChange"></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="12">-->
<!--            <a-form-model-item label="英文名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="form.nameEn" placeholder="请输入英文名称" @change="onChange"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="12">
            <a-form-model-item label="受托人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="form.contactPerson" placeholder="请输入受托人" @change="onChange"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="受托人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="form.contactPhone" placeholder="请输入受托人电话"
                       @change="onChange"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="注册地址" :labelCol="{  xs: { span: 24 }, sm: { span: 2 } }"
                               :wrapperCol="{ xs: { span: 24 }, sm: { span: 21 }}">
              <a-input v-model="form.address" placeholder="请输入注册地址" @change="onChange"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="法人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="form.legal" placeholder="请输入法人" @change="onChange"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="信用代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="form.creditCode" placeholder="请输入信用代码" @change="onChange"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item :label="memberType==='0'?'付款行':'收款行'" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="form.bank" placeholder="请输入开户行" @change="onChange"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="银行账号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="form.bankNo" placeholder="请输入开户行号" @change="onChange"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="账户名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="form.bankName" placeholder="请输入开户行名称" @change="onChange"></a-input>
            </a-form-model-item>
          </a-col>
          <template v-if="memberType!=='0'">
            <a-col :span="12">
              <a-form-model-item label="合同金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-model="form.amount" placeholder="请输入合同金额" style="width: 100%"
                                @change="calculate"></a-input-number>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="币种" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-model="form.coin" dictCode="coin_type" @input="onChange"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="已付金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-model="form.payAmount" placeholder="请输入已付金额" style="width: 100%"
                                @change="calculate"></a-input-number>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="预付比例" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="form.lockAmount" placeholder="请输入预付比例" @change="onChange" suffix="%"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="剩余金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-model="form.restAmount" placeholder="请输入剩余金额" style="width: 100%"
                                @change="onChange"></a-input-number>
              </a-form-model-item>
            </a-col>
          </template>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'ContractMemberForm',
    components: {},
    model: {
      prop: 'form',
      event: 'change'
    },
    props: {
      form: {
        type: Object,
        default: () => ({}),
        require: true
      },
      memberType: {
        type: String,
        default: '',
        require: true
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
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 18 }
        },
        confirmLoading: false,
        validatorRules: {},
        url: {
          add: '/contract/contractMember/add',
          edit: '/contract/contractMember/edit',
          queryById: '/contract/contractMember/queryById'
        }
      }
    },
    computed: {},
    created() {
    },
    methods: {
      onChange(val) {
        this.$emit('change', this.form)
      },
      calculate(val) {
        this.form.restAmount = this.form.amount - this.form.payAmount
        this.onChange(val)
      }
    }
  }
</script>