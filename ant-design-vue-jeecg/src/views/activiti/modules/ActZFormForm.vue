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
          <a-col :span="12" v-if="!!model.id">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-button type="primary" class="editable-add-btn" @click="jsAdd(model.id)">增强JS</a-button>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <!--   列表-->
    <template v-if="!!model.id">
      <ActZFieldGroup ref="groupTable" :table-id="model.tableMetaId" :data-id="model.id"></ActZFieldGroup>
      <h3 v-show="tableMeta.tableType===2" style="margin-top: 8px;margin-bottom: 8px">子表</h3>
      <a-table ref="table"
               size="middle"
               bordered
               rowKey="id"
               :scroll="{x:true}"
               v-show="tableMeta.tableType===2"
               :columns="columns"
               :pagination="false"
               :data-source="subTableList">
        <template slot="action" slot-scope="text, record">
          <a @click="showModal(record.id)">字段组</a>
          <a-divider type="vertical"/>
          <a @click="jsAdd(model.id.slice(16)+record.id.slice(16))">增强JS</a>
        </template>
      </a-table>
      <!--      字段组模态框-->
      <a-modal :width="800" v-model="fieldGroupVisible" title="字段分组">
        <ActZFieldGroup :table-id="subTableId" :data-id="model.id+subTableId"></ActZFieldGroup>
      </a-modal>
    </template>
    <a-modal :width="800" :visible="editorVisible" title="JS增强" @ok="handleOk" @cancel="editorVisible=false">
      <j-code-editor ref="jcodeEditor" language="javascript" v-model="editorValue" :fullScreen="true"/>
    </a-modal>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import ActZFieldGroup from '../components/ActZFieldGroup'

  export default {
    name: 'ActZFormForm',
    components: { ActZFieldGroup },
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
          add: '/process/actZForm/add',
          edit: '/process/actZForm/edit',
          queryById: '/process/actZForm/queryById',
          tableList: '/online/cgform/head/list',
          list: '/process/actZForm/list',
          ruleCode: '/sys/fillRule/executeRuleByCode/',
          subTable: '/process/actZForm/subTable',
          enhanceJs: '/online/cgform/head/enhanceJs/'
        },
        tableOptions: [],
        lastCode: '',
        tableMeta: {},
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '表名',
            align: 'center',
            dataIndex: 'tableName'
          },
          {
            title: '表描述',
            align: 'center',
            dataIndex: 'tableTxt'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            fixed: 'right',
            width: 147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        subTableList: [],
        subTableId: '',
        fieldGroupVisible: false,
        editorVisible: false,
        editorValue: '',
        enhanceJs: {}
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
      showModal(id) {
        this.fieldGroupVisible = true
        this.subTableId = id
      },
      jsAdd(id) {
        this.editorVisible = true
        this.enhanceJs.cgformHeadId = id
        this.enhanceJs.cgJsType = 'process'
        getAction(this.url.enhanceJs + id, { type: 'process' }).then(res => {
          if (res.success && res.result) {
            this.enhanceJs = res.result
            this.$refs.jcodeEditor.setCodeContent(res.result.cgJs)
          } else {
            this.$refs.jcodeEditor.setCodeContent('')
          }
        })
      },
      handleOk() {
        let methods = ''
        if (this.enhanceJs.id) {
          methods = 'put'
        } else {
          methods = 'post'
        }
        this.enhanceJs.cgJs = this.editorValue
        httpAction(this.url.enhanceJs + this.enhanceJs.cgformHeadId, this.enhanceJs, methods).then(res => {
          if (res.success) {
            this.$message.success('保存成功')
            this.editorVisible = false
            this.editorValue = ''
          }
        })
      },
      /*获取子表*/
      getSubTable() {
        getAction(this.url.subTable, { metaId: this.model.tableMetaId }).then(res => {
          this.subTableList = res.result
        })
      },
      /*获取最后一个编码*/
      getLastCode() {
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
      /*获取新编码*/
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
          this.tableMeta = table
          this.getSubTable()
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
          if (this.model.id) {
            this.tableMeta = _.find(this.tableOptions, { id: this.model.tableMetaId })
          }
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
        if (record) {
          this.getSubTable()
          this.getTableOptions(record.tableType)
        }
      },
      /*提交表单*/
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
<style scoped>
  .full-screen-parent /deep/ .code-editor-cust {
    height: 300px;
  }
</style>