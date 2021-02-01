import pick from 'lodash.pick'

export const activitiApproveMixin = {
  props: {
    /*全局禁用，可表示查看*/
    disabled: {
      type: Boolean,
      default: false,
      required: false
    },
    /*展示条件*/
    showCondition: {
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
  component() {
  },
  data() {
    return {
      url: {
        getForm: '/actBusiness/getForm',
        addApply: '/actBusiness/add',
        editForm: '/actBusiness/editForm'
      },
      /*表单回显数据*/
      form: {},
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
          this.form = res.result
          this.form.tableName = r.tableName
          console.log('表单回显数据', this.form)
          this.btndisabled = false
        } else {
          this.$message.error(res.message)
        }
      })
    },
    // 需要具体实现,需要保存到表的字段“,”分割
    pickValue() {
    },
    // handler
    handleSubmit(e) {
      e.preventDefault()
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          this.form.procDefId = this.processData.id
          this.form.procDeTitle = this.processData.name
          if (!this.form.tableName) {
            console.log(this.processData)
            this.form.tableName = this.processData.businessTable
          }
          this.form.params = this.handleParams(this.form)
          this.pickValue()
          console.log('formData', this.form)
          var url = this.url.addApply
          if (!this.isNew) {
            url = this.url.editForm
          }
          this.btndisabled = true
          this.postDataAction(url, this.form).then((res) => {
            if (res.success) {
              this.$message.success('保存成功！')
              //todo 将表单的数据传给父组件
              this.$emit('afterSubmit', this.form)
            } else {
              this.$message.error(res.message)
            }
          }).finally(() => {
            this.btndisabled = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    /*处理参数*/
    handleParams(values) {
      return _.keys(values.params).map(key => ({
        paramsKey: key,
        paramsVal: values.params[key]
      }))
    }
  }
}