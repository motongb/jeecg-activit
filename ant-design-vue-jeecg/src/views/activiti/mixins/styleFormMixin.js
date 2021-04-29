import { getAction, postAction, httpAction } from '@/api/manage'
import FormItemType from '../components/FormItemType'
export const styleFormMixin = {
  props: {
    formId: {
      type: String,
      default: null
    },
    model: {
      type: Object,
      default: () => ({})
    },
    formDisabled: {
      type: Boolean,
      default: false
    },
    isNew: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    formId: {
      immediate: true,
      handler(val) {
        if (this.formId) {
          this.getFormItem(this.formId)
        }
      }
    }
  },
  data() {
    return {
      FormItemType,
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
      url: {
        getFormItem: '/online/cgform/api/getErpFormItem/'
      },
      fieldList: [],
      properties: {},
      validatorRules: {}
    }
  },
  created() {

  },
  methods: {
    validate(callback) {
      this.$refs.form.validate(valid => callback(valid))
    },
    clearValidate() {
      this.$refs.form.clearValidate()
    },
    getFields(properties) {
      let list = []
      for (let key in properties) {
        list.push({ field: key, order: properties[key].order })
      }
      list.sort((a, b) => a.order - b.order)
      return list.map(m => m.field)
    },
    getFieldList() {
      return this.fieldList
    },
    getFormItem(formId) {
      getAction(this.url.getFormItem + formId, {}).then(res => {
        this.formMeta = res.result
        // 获取属性
        this.properties = this.formMeta.schema ? this.formMeta.schema.properties : {}
        // 获取字段
        this.fieldList = this.getFields(this.properties)
        // 设置校验字段
        if (this.formMeta.schema.required) {
          this.formMeta.schema.required.forEach(f => this.validatorRules[f] = [{
            required: true,
            message: `请输入${this.properties[f].title}!`
          }])
        }
        this.$emit('formItemInitial', { fieldList: this.fieldList, properties: this.properties })
      })
    },
    popupCallback(value, row) {
      this.model = Object.assign(this.model, row)
    },
    handleCategoryChange(value, backObj) {
      this.model = Object.assign(this.model, backObj)
    }
  }
}