<template>
  <a-spin :spinning="confirmLoading">
    <a-form-model ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-model-item label="中文名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.nameCn" placeholder="请输入中文名称"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="英文名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.nameEn" placeholder="请输入英文名称"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.contactPerson" placeholder="请输入联系人"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="联系人电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.contractPhone" placeholder="请输入联系人电话"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="24">
          <a-form-model-item label="注册地址" :labelCol="{  xs: { span: 24 }, sm: { span: 2 } }" :wrapperCol="{ xs: { span: 24 }, sm: { span: 21 }}">
            <a-input v-model="form.address" placeholder="请输入注册地址"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="法人" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.legal" placeholder="请输入法人"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="信用代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.creditCode" placeholder="请输入信用代码"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="开户行" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.bank" placeholder="请输入开户行"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="开户行号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.bankNo" placeholder="请输入开户行号"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="开户行名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.bankName" placeholder="请输入开户行名称"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="合同金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.amount" placeholder="请输入合同金额"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="币种" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.coin" placeholder="请输入币种"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="已付金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.payAmount" placeholder="请输入已付金额"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="锁定金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.lockAmount" placeholder="请输入锁定金额"></a-input>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item label="剩余金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-model="form.restAmount" placeholder="请输入剩余金额"></a-input>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
  </a-spin>
</template>

<script>

  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'ContractMemberForm',
    components: {},
    model: {
      prop: 'form',
      event: 'on-change'
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
    methods: {}
  }
</script>