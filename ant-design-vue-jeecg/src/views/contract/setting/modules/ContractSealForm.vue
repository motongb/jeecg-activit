<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-item label="印章名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['name',{rules: [{ required: true, message: '请输入印章名称' }]}]"
                       placeholder="请输入印章名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['format']" placeholder="请输入规格"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="使用人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-select-multi-user v-decorator="['users']" ></j-select-multi-user>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group
                v-decorator="['status',{initialValue:'0'}]">
                <a-radio value="0">启用</a-radio>
                <a-radio value="1">禁用</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="所属" :labelCol="labelCol" :wrapperCol="wrapperCol"  :help="help" :validateStatus="validateStatus">
              <a-input-group compact>
                <a-input-search style="width: 70%" v-decorator="['belongName']" placeholder="请选择所属"
                                @search="belongSearch"/>
                <j-dict-select-tag style="width: 30%" v-decorator="['belongType', {initialValue:'1'}]"
                                   @change="belongChange"
                                   :triggerChange="true" dictCode="seal_belong_type"/>
              </a-input-group>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="防伪码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['fakeCode']" placeholder="请输入防伪码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="印章图片" :label-col="{ span: 2 }" :wrapper-col="{ span: 20 }">
              <j-upload fileType="image" :number="1" v-decorator="['fileUrl']"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
    <company-select ref="companySelect" @selected="handleSelected"></company-select>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import CompanySelect from '../../components/CompanySelect'

  export default {
    name: 'ContractSealForm',
    components: { CompanySelect },
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
        // 自定义校验信息
        validateStatus: '',
        help: '',
        form: this.$form.createForm(this),
        model: {},
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
          add: '/contract/contractSeal/add',
          edit: '/contract/contractSeal/edit',
          queryById: '/contract/contractSeal/queryById'
        }
      }
    },
    computed: {
      formDisabled() {
        return this.disabled
      }
    },
    created() {

    },
    methods: {
      belongChange(item) {
        let belongType = this.form.getFieldValue('belongType')
        if (item != belongType) {
          this.form.resetFields(['belongName'])
          this.model.belong = ''
        }
      },
      handleSelected(item) {
        console.log(item)
        this.form.setFieldsValue({ belongName: item.name })
        this.model.belong = item.id
      },
      belongSearch() {
        let belongType = this.form.getFieldValue('belongType')
        if (belongType == '1') {
          this.$refs.companySelect.show()
        }
      },
      add() {
        this.edit({})
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'name', 'format', 'users', 'status', 'fileUrl', 'fakeCode', 'belongType', 'belongName'))
        })
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
        this.form.setFieldsValue(pick(row, 'name', 'sealCode', 'format', 'useNum', 'users', 'status', 'fileUrl', 'belong', 'fakeCode', 'belongType', 'belongName'))
      }
    }
  }
</script>