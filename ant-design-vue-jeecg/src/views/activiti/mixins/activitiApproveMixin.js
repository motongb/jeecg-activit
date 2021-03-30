import { getAction, postAction } from '@/api/manage'

export const activitiApproveMixin = {
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
    },
    /*展示条件*/
    showCondition: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      url: {
        getForm: '/actBusiness/getForm',
        addApply: '/actBusiness/add',
        editForm: '/actBusiness/editForm'
      },
      /*表单回显数据*/
      form: {}
    }
  },
  created() {
    console.log('流程数据', this.lcModa)
    if (!this.lcModa.isNew) {
      this.init()
    }
  },
  methods: {
    /*回显数据*/
    init() {
      let r = this.lcModa.processData
      getAction(this.url.getForm, { tableId: r.tableId, tableName: r.tableName }).then((res) => {
        if (res.success) {
          this.form = res.result
          this.form.tableName = r.tableName
          console.log('表单回显数据', this.form)
        } else {
          this.$message.error(res.message)
        }
      })
    },
    pickValue() {

    },
    // handler
    submitForm() {
      return new Promise((resolve, reject) => {
        this.$refs.ruleForm.validate(valid => {
          if (valid) {
            this.form.title = this.lcModa.title
            this.form.procDefId = this.lcModa.processData.id
            this.form.procDeTitle = this.lcModa.processData.name
            if (!this.form.tableName) {
              this.form.tableName = this.lcModa.processData.businessTable
            }
            this.pickValue()
            console.log('formData', this.form)
            let url = this.url.addApply
            if (!this.isNew) {
              url = this.url.editForm
            }
            postAction(url, this.form).then((res) => {
              if (res.success) {
                this.$message.success('保存成功！')
                //todo 将表单的数据传给父组件
                resolve(this.form)
              } else {
                this.$message.error(res.message)
              }
            }).finally(() => {
              reject()
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