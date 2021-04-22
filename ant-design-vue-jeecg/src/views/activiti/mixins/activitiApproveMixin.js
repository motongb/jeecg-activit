import { getAction, postAction, httpAction } from '@/api/manage'

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
    },
    preView: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      FormItemType: {
        text: 'text',
        date: 'date',
        textarea: 'textarea',
        sel_depart: 'sel_depart',
        pca: 'pca',
        sel_tree: 'sel_tree',
        sel_user: 'sel_user',
        image: 'image',
        password: 'password',
        popup: 'popup',
        umeditor: 'umeditor',
        checkbox: 'checkbox',
        file: 'file',
        cat_tree: 'cat_tree',
        list_multi: 'list_multi',
        list: 'list',
        markdown: 'markdown',
        radio: 'radio',
        switch: 'switch',
        sel_search: 'sel_search',
        time: 'time'
      },
      url: {
        getForm: '/actBusiness/getForm',
        addApply: '/actBusiness/add',
        editForm: '/actBusiness/editForm',
        getFormItem: '/online/cgform/api/getFormItem/',
        tableForm: '/activiti/actZForm/list'
      },
      confirmLoading: false,
      /*表单回显数据*/
      model: {},
      formMeta: {
        schema: {
          $schema: 'http://json-schema.org/draft-07/schema#',
          describe: '流程测试表单',
          type: 'object',
          title: '我是一个jsonschema title',
          required: ['text'],
          properties: {},
          table: 'ces_process_form'
        },
        head: {
          id: '',
          tableName: 'ces_process_form',
          tableType: 1,
          tableVersion: 9,
          tableTxt: '流程测试表单',
          isCheckbox: 'Y',
          isDbSynch: 'Y',
          isPage: 'Y',
          isTree: 'N',
          idSequence: null,
          idType: 'UUID',
          queryMode: 'single',
          relationType: null,
          subTableStr: null,
          tabOrderNum: null,
          treeParentIdField: null,
          treeIdField: null,
          treeFieldname: null,
          formCategory: 'temp',
          formTemplate: '1',
          themeTemplate: 'normal',
          formTemplateMobile: null,
          updateBy: 'admin',
          updateTime: '2021-04-21 15:35:27',
          createBy: 'admin',
          createTime: '2021-04-21 09:43:52',
          copyType: 0,
          copyVersion: null,
          physicId: null,
          hascopy: null,
          scroll: 1,
          taskId: null,
          isDesForm: 'N',
          desFormCode: null
        },
        formTemplate: '1'
      },
      validatorRules: {},
      fieldList: [],
      properties: {}
    }
  },
  computed: {},
  beforeCreate() {

  },
  created() {
    console.log('流程数据', this.lcModa)
    if (!this.lcModa.isNew && !this.preView) {//编辑
      this.getForm()
    }
    this.getTableForm()
  },
  methods: {
    getTableForm() {
      getAction(this.url.tableForm, {
        code: this.lcModa.processData.formCode,
        column: 'createTime',
        order: 'desc'
      }).then(res => {
        if (res.result.records.length > 0) {
          let tableForm = res.result.records[0]
          this.getFormItem(tableForm.tableMetaId)
        }
      })
    },
    getFormItem(metaId) {
      getAction(this.url.getFormItem + metaId, {}).then(res => {
        this.formMeta = res.result
        // 获取属性
        this.properties = this.formMeta.schema ? this.formMeta.schema.properties : {}
        // 获取字段
        this.fieldList = Object.keys(this.properties)
        // 设置默认值
        if (this.lcModa.isNew) {
          // this.$nextTick(() => {
          //   for (let f of this.fieldList) {
          //     if (this.properties[f].defVal) {
          //       this.model[f] = this.properties[f].defVal
          //     }
          //   }
          // })
        }
        // 设置校验字段
        if (this.formMeta.schema.required){
          this.formMeta.schema.required.forEach(f => this.validatorRules[f] = [{
            required: true,
            message: `请输入${this.properties[f].title}!`
          }])
        }
        console.log(res.result, this.properties, this.fieldList)
      })
    },
    /*回显数据*/
    getForm() {
      let r = this.lcModa.processData
      getAction(this.url.getForm, { tableId: r.tableId, tableName: r.tableName }).then((res) => {
        if (res.success) {
          this.model = res.result
          this.model.tableName = r.tableName
          console.log('表单回显数据', this.model)
        } else {
          this.$message.error(res.message)
        }
      })
    },
    // handler
    submitForm() {
      return new Promise((resolve, reject) => {
        this.$refs.form.validate(valid => {
          if (valid) {
            this.model.title = this.lcModa.title
            this.model.procDefId = this.lcModa.processData.id
            this.model.procDeTitle = this.lcModa.processData.name
            this.model.filedNames = this.fieldList.join(',')
            if (!this.model.tableName) {
              this.model.tableName = this.lcModa.processData.tableName
            }
            console.log('formData', this.model)
            let url = ''
            if (this.lcModa.isNew) {
              url = this.url.addApply
            } else {
              url = this.url.editForm
            }
            postAction(url, this.model).then((res) => {
              if (res.success) {
                this.$message.success('保存成功！')
                resolve(this.model)
              } else {
                this.$message.error(res.message)
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