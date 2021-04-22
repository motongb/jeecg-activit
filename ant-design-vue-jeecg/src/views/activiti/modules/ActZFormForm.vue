<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="表单标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入表单标题"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="表单编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
              <a-input v-model="model.code" placeholder="请输入表单编码"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <j-dict-select-tag type="list" v-model="model.type" dictCode="act_z_form_type" placeholder="请选择类型"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="表单样式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="style">
              <j-dict-select-tag v-if="model.type=='1'" type="list" v-model="model.style" dictCode="act_z_form_style"
                                 placeholder="请选择表单样式"/>
              <a-input v-else v-model="model.style" placeholder="请输入路由名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="数据库表类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tableType">
              <j-dict-select-tag @input="getTableOptions" v-model="model.tableType" dictCode="ol_form_biz_type"
                                 placeholder="请选择数据库表类型"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="数据库表名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tableName">
              <a-select placeholder="选择数据库表名" v-model="model.tableName" show-search option-filter-prop="children"
                        @change="handleTableChange"
                        :filter-option="filterOption">
                <template v-for="item in tableOptions">
                  <a-select-option :value="item.tableName">{{item.tableTxt}}</a-select-option>
                </template>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <j-dict-select-tag type="radio" v-model="model.status" dictCode="yn" placeholder="请选择启用"/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'ActZFormForm',
    components: {},
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
        model: {
          style: 'not',
          type: '1',
          status: 1,
          code: ''
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        validatorRules: {},
        url: {
          add: '/activiti/actZForm/add',
          edit: '/activiti/actZForm/edit',
          queryById: '/activiti/actZForm/queryById',
          tableList: '/online/cgform/head/list',
          list: '/activiti/actZForm/list',
          ruleCode: '/sys/fillRule/executeRuleByCode/'
        },
        tableOptions: [],
        lastCode: ''
      }
    },
    computed: {
      formDisabled() {
        return this.disabled
      }
    },
    created() {
      //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model))
    },
    methods: {
      getLastCode() {
        // let url = '/online/cgform/api/getData/746a8e969df74571936b094b1590c4d3'
        let params = {
          column: 'code',
          order: 'desc',
          pageNo: 1,
          pageSize: 1
        }
        getAction(this.url.list, params).then(res => {
          let r = res.result.records[0]
          if (r) {
            this.lastCode = r.code
            this.getNewCode()
          }
        })
      },
      getNewCode() {
        let ruleCode = 'flow_number'
        let formData = {
          lastCode: this.lastCode,
          firstNo: 'C099',
          numberLength: 3
        }
        httpAction(this.url.ruleCode + ruleCode, formData, 'post').then(res => {
          if (res.success) {
            this.model.code = res.result
          }
        })
      },
      handleTableChange(tableName) {
        let table = _.find(this.tableOptions, { tableName })
        if (table) {
          this.model.tableMetaId = table.id
        }
      },
      getTableOptions(value) {
        getAction(this.url.tableList, {
          pageNo: 1,
          pageSize: 99,
          copyType: 0,
          formCategory: value
        }).then(res => {
          this.tableOptions = res.result.records
        })
      },
      filterOption(input, option) {
        return (
          option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
        )
      },
      add() {
        this.edit(this.modelDefault)
        this.getLastCode()
      },
      edit(record) {
        this.model = Object.assign({}, record)
        this.visible = true
        if (record) {
          this.getTableOptions(record.tableType)
        }
      },
      submitForm() {
        const that = this
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            httpAction(httpurl, this.model, method).then((res) => {
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
      }
    }
  }
</script>