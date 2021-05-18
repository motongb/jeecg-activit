import { getAction, postAction, httpAction } from '@/api/manage'

export const activitiApproveMixin = {
  props: {
    lcModa: {
      type: Object,
      default: () => ({
        formCode: '',//表单编码
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
    },
    preView: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    formDisabled() {
      return this.lcModa.disabled
    }
  },
  data() {
    return {
      url: {
        getForm: '/actBusiness/getFormData/',
        // getForm: '/online/cgform/api/form/',
        addApply: '/actBusiness/add',
        editForm: '/actBusiness/editForm',
        tableForm: '/process/actZForm/queryByCode/',
        formList: '/process/actZForm/list',
        getErpColumn: '/online/cgform/api/getErpColumns/'
      },
      confirmLoading: false,
      /*表单回显数据*/
      model: {},
      modelDefault: {},
      mainFormMeta: {},
      subFormMeta: [],
      processForm: {}, //流程表单
      fieldList: [],
      properties: {},
      enhanceJs: ''
    }
  },
  created() {
    console.log('流程数据', this.lcModa)
    this.getTableForm()
  },
  methods: {
    onFormItemInitial({ fieldList, properties }) {
      this.fieldList = fieldList
      this.properties = properties
      // 设置默认值
      if (!this.model.id) {
        this.$nextTick(() => {
          for (let f of fieldList) {
            if (properties[f].defVal) {
              this.$set(this.model, f, properties[f].type === 'number' ? Number(properties[f].defVal) : properties[f].defVal)
            }
          }
        })
      }
    },
    getTableForm() {
      getAction(this.url.tableForm + this.lcModa.processData.formCode).then(res => {
        if (res.result) {
          this.processForm = res.result
          this.getErpColumn(this.processForm.tableMetaId)
          if (!this.lcModa.isNew && !this.preView) {//编辑
            this.getForm()
          }
        } else {
          this.$message.warning(`编码${this.lcModa.processData.formCode}没有配置表单`)
        }
      })
    },
    getErpColumn(mainId) {
      getAction(this.url.getErpColumn + mainId, {}).then(res => {
        this.subFormMeta = res.result.subList
        this.mainFormMeta = res.result.main
      })
    },
    /*回显数据*/
    getForm() {
      getAction(this.url.getForm + this.processForm.tableMetaId + '/' + this.lcModa.processData.tableId, {}).then((res) => {
        if (res.success) {
          this.model = res.result
          console.log('表单回显数据', this.model)
        } else {
          this.$message.error(res.message)
        }
      })
    },
    // handler
    submitForm() {
      const that = this
      return new Promise((resolve, reject) => {
        this.$refs.mainForm.validate(valid => {
          if (valid) {
            let processFormData = {}
            processFormData.id = this.model.id
            processFormData.title = that.lcModa.title
            processFormData.procDefId = that.lcModa.processData.id
            processFormData.procDeTitle = that.lcModa.processData.name
            processFormData.tableName = that.lcModa.processData.tableName
            processFormData.mainFields = this.$refs.mainForm.getFieldList()
            if (that.subFormMeta && that.subFormMeta.length > 0) {
              let subFormMeta = []
              that.subFormMeta.forEach(sub => {
                processFormData[sub.currentTableName] = that.$refs[sub.currentTableName][0].getDataSource()
                subFormMeta.push({
                  tableName: sub.currentTableName,
                  fields: that.$refs[sub.currentTableName][0].getFieldList(),
                  type: 'list',
                  pkey: sub.foreignKeys[0].key,
                  pField: sub.foreignKeys[0].field
                })
              })
              processFormData.subFormMeta = subFormMeta
            }
            processFormData.model = that.model
            console.log('formData', processFormData)
            let url = ''
            if (that.lcModa.isNew) {
              url = that.url.addApply
            } else {
              url = that.url.editForm
            }
            postAction(url, processFormData).then((res) => {
              if (res.success) {
                that.$message.success('保存成功！')
                resolve(that.model)
              } else {
                that.$message.error(res.message)
              }
            }).finally(() => {
              reject()
            })
          } else {
            reject()
          }
        })
      })
    }
  }
}