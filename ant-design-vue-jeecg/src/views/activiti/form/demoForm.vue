<template>
  <!--流程业务表单-->
  <div class="form-main">
    <a-form @submit="handleSubmit" :form="form">
      <a-form-item
        label="名称"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }">
        <a-input :disabled="disabled"
                 v-decorator="[
            'name',
            {rules: [{ required: true, message: '请输入名称' }]}
          ]"
                 name="name"
                 placeholder="给目标起个名字"/>
      </a-form-item>
      <template v-if="!task">
        <a-form-item
          label="线路选择"
          :labelCol="{lg: {span: 7}, sm: {span: 7}}"
          :wrapperCol="{lg: {span: 10}, sm: {span: 17} }">
          <a-radio-group v-model="formParams.condition">
            <a-radio value="路线一">路线一</a-radio>
            <a-radio value="路线二">路线二</a-radio>
          </a-radio-group>
        </a-form-item>
      </template>
      <a-form-item v-if="!disabled"
                   :wrapperCol="{ span: 24 }"
                   style="text-align: center">
        <a-button type="primary" :disabled="disabled||btndisabled" @click="handleSubmit">保存</a-button>
        <a-button style="margin-left: 8px" :disabled="disabled" @click="close">取消</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
  import pick from 'lodash.pick'

  export default {
    name: 'demoForm',
    props: {
      /*全局禁用，可表示查看*/
      disabled: {
        type: Boolean,
        default: false,
        required: false
      },
      /*展示条件*/
      showCondition:{
        type: Boolean,
        default: false
      },
      /*流程数据*/
      processData: {
        type: Object,
        default: () => {
          return {}
        },
        required: false
      },
      /*是否新增*/
      isNew: { type: Boolean, default: false, required: false },
      /*是否处理流程*/
      task: { type: Boolean, default: false, required: false }
    },
    data() {
      return {
        url: {
          getForm: '/actBusiness/getForm',
          addApply: '/actBusiness/add',
          editForm: '/actBusiness/editForm'
        },
        description: '流程表单demo，按例开发表单。需在 activitiMixin.js 中加入写好的表单',
        // form
        form: this.$form.createForm(this),
        formParams: { condition: '路线一' },
        /*表单回显数据*/
        data: {},
        btndisabled: false
      }
    },
    created() {
      console.log('流程数据', this.processData)
      if (!this.isNew) {
        this.init()
      }
    },
    methods: {
      /*回显数据*/
      init() {
        this.btndisabled = true
        var r = this.processData
        this.getAction(this.url.getForm, {
          tableId: r.tableId,
          tableName: r.tableName
        }).then((res) => {
          if (res.success) {
            let formData = res.result
            formData.tableName = r.tableName
            this.data = formData
            console.log('表单回显数据', this.data)
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.data, 'name'))
            })
            this.btndisabled = false
          } else {
            this.$message.error(res.message)
          }
        })
      },
      // handler
      handleSubmit(e) {
        e.preventDefault()
        this.form.validateFields((err, values) => {
          if (!err) {
            let formData = Object.assign(this.data || {}, values)
            formData.procDefId = this.processData.id
            formData.procDeTitle = this.processData.name
            if (!formData.tableName) formData.tableName = this.processData.businessTable
            formData.filedNames = _.keys(values).join(',')
            formData.params = this.handleParams()
            console.log('formData', values)

            var url = this.url.addApply
            if (!this.isNew) {
              url = this.url.editForm
            }
            this.btndisabled = true
            this.postDataAction(url, formData).then((res) => {
              if (res.success) {
                this.$message.success('保存成功！')
                //todo 将表单的数据传给父组件
                this.$emit('afterSubmit', formData)
              } else {
                this.$message.error(res.message)
              }
            }).finally(() => {
              this.btndisabled = false
            })
          }
        })
      },
      close() {
        //todo 关闭后的回调
        this.$emit('close')
      },
      /*处理参数*/
      handleParams() {
        return _.keys(this.formParams).map(key => ({
          paramsKey: key,
          paramsVal: this.formParams[key]
        }))
      }
    }
  }
</script>
<style lang="less" scoped>
  .form-main {
  }
</style>