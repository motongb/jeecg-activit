<template>
  <j-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleSubmit"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>
          <a-col :xs="24" :sm="12">
            <a-form-item label="规则名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入规则名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-item label="规则表" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select placeholder="选择表" v-decorator="['ruleTable', validatorRules.ruleTable]" show-search
                        option-filter-prop="children" :filter-option="filterOption" @change="handleTableSelected">
                <template v-for="item in tableOptions">
                  <a-select-option :value="item.tableName">{{item.tableTxt}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-item label="规则字段" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select placeholder="选择表字段" v-decorator="['ruleField', validatorRules.ruleField]" show-search
                        option-filter-prop="children" :filter-option="filterOption">
                <template v-for="item in fieldOptions">
                  <a-select-option :value="item.dbFieldName">{{item.dbFieldTxt}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="自定义规则项" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="sysRuleItemTable.loading"
            :columns="sysRuleItemTable.columns"
            :dataSource="sysRuleItemTable.dataSource"
            :dragSort="true"
            dragSortKey="sort"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"
            @valueChange="handleRowChange">
            <template v-slot:itemVal="props">
              <!--              日期-->
              <template
                v-if="sysRuleItemTable.dataSource[props.index]&&sysRuleItemTable.dataSource[props.index].itemType==='1'">
                <a-input-group compact>
                  <a-select placeholder="选择日期格式" style="width: 80%" v-model="props.text"
                            @change="(val,options)=>handleValueChange(val,props)">
                    <template v-for="item in dictOptions">
                      <a-select-option :value="item.value">{{item.text}}</a-select-option>
                    </template>
                  </a-select>
                  <div style="width: 20%"><p style="margin: 5px 0px 0px 12px">格式</p></div>
                </a-input-group>
              </template>
              <!--              部门编码-->
              <template
                v-else-if="sysRuleItemTable.dataSource[props.index]&&sysRuleItemTable.dataSource[props.index].itemType==='2'">
                <a-input-group compact>
                  <a-input style="width: 80%" v-model="props.text"
                           @change="e=>handleValueChange(e.target.value,props)"></a-input>
                  <div style="width: 20%"><p style="margin: 5px 0px 0px 12px">级别</p></div>
                </a-input-group>
              </template>
              <!--              字符串常量-->
              <template
                v-else-if="sysRuleItemTable.dataSource[props.index]&&sysRuleItemTable.dataSource[props.index].itemType==='3'">
                <a-input style="width: 80%" v-model="props.text"
                         @change="e=>handleValueChange(e.target.value,props)"></a-input>
              </template>
              <!--              流水号-->
              <template
                v-else-if="sysRuleItemTable.dataSource[props.index]&&sysRuleItemTable.dataSource[props.index].itemType==='4'">
                <a-input-group compact>
                  <a-input-number style="width: 80%" v-model="props.text" :min="1" :max="10"
                                  @change="val=>handleValueChange(val,props)"/>
                  <div style="width: 20%"><p style="margin: 5px 0px 0px 12px">位数</p></div>
                </a-input-group>
              </template>
              <!--              流水号重置方式-->
              <template
                v-else-if="sysRuleItemTable.dataSource[props.index]&&sysRuleItemTable.dataSource[props.index].itemType==='5'">
                <a-select placeholder="选择重置方式" style="width: 80%" v-model="props.text"
                          @change="(val,options)=>handleValueChange(val,props)">
                  <template v-for="item in flowDictOptions">
                    <a-select-option :value="item.value">{{item.text}}</a-select-option>
                  </template>
                </a-select>
              </template>
              <a-input v-else/>
            </template>
          </j-editable-table>
        </a-tab-pane>

      </a-tabs>

    </a-spin>
  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import { ajaxGetDictItems, getDictItemsFromCache } from '@/api/api'
  import { getAction } from '@/api/manage'

  export default {
    name: 'SysRuleModal',
    mixins: [JEditableTableMixin],
    components: {},
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 }
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 }
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          name: {
            rules: [
              { required: true, message: '请输入规则名称!' }
            ]
          },
          ruleField: {
            rules: [
              { required: true, message: '请输入规则字段!' }
            ]
          },
          ruleTable: {
            rules: [
              { required: true, message: '请输入规则表!' }
            ]
          }
        },
        refKeys: ['sysRuleItem'],
        tableKeys: ['sysRuleItem'],
        activeKey: 'sysRuleItem',
        // 自定义规则项
        sysRuleItemTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '规则项类型',
              key: 'itemType',
              type: FormTypes.select,
              width: '200px',
              placeholder: '请输入${title}',
              dictCode: 'rule_item_type',
              defaultValue: ''
            },
            {
              title: '项值',
              key: 'itemVal',
              type: FormTypes.slot, // 定义该列为 自定义插值列
              slotName: 'itemVal', // slot 的名称，对应 v-slot 冒号后面和等号前面的内容
              width: '250px'
            },
            {
              title: '排序',
              key: 'sort',
              type: FormTypes.input,
              width: '200px',
              placeholder: '请输入${title}',
              defaultValue: '',
              disabled: true
            }
          ]
        },
        url: {
          add: '/contract/sysRule/add',
          edit: '/contract/sysRule/edit',
          tableList: '/online/cgform/head/list',
          fieldList: '/online/cgform/field/listByHeadId',
          sysRuleItem: {
            list: '/contract/sysRule/querySysRuleItemByMainId'
          }
        },
        dictOptions: [], //日期格式
        flowDictOptions: [], // 流水号重置方式
        tableOptions: [], // 表
        fieldOptions: [] // 字段
      }
    },
    created() {
      this.initDictData()
      this.getTableOptions()
    },
    methods: {
      filterOption(input, option) {
        return (
          option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
        )
      },
      /*选择表*/
      handleTableSelected(tableName) {
        let table = this.tableOptions.find(m => m.tableName === tableName)
        console.log(table)
        getAction(this.url.fieldList, { headId: table.id }).then(res => {
          this.fieldOptions = res.result
        })
      },
      getTableOptions() {
        getAction(this.url.tableList, {
          pageNo: 1,
          pageSize: 99,
          copyType: 0,
          formCategory: 'contract'
        }).then(res => {
          this.tableOptions = res.result.records
        })
      },
      /*提交*/
      handleSubmit() {
        this.handleOk()
        this.sysRuleItemTable.dataSource = []
      },
      /*取消*/
      handleCancel() {
        this.visible = false
        this.sysRuleItemTable.dataSource = []
      },
      /*初始化字典*/
      initDictData() {
        //从缓存中读取字典配置
        this.dictOptions = getDictItemsFromCache('date_format')
        this.flowDictOptions = getDictItemsFromCache('flow_number_reset')
      },
      /*子表行改变*/
      handleRowChange({ type, row, column, value, target }) {
        if (column.key === 'itemType') {
          if (!this.sysRuleItemTable.dataSource[row.sort - 1]) {
            this.sysRuleItemTable.dataSource[row.sort - 1] = { id: row.id, itemType: value }
          } else {
            this.sysRuleItemTable.dataSource[row.sort - 1].itemType = value
          }
        }
      },
      /*表单元格值改变*/
      handleValueChange(val, props) {
        let { rowId, target } = props
        target.setValues([{ rowKey: rowId, values: { itemVal: val } }])
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model, 'name', 'ruleField', 'ruleTable')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.sysRuleItem.list, params, this.sysRuleItemTable)
          this.handleTableSelected(this.model.ruleTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          sysRuleItemList: allValues.tablesValue[0].values
        }
      },
      validateError(msg) {
        this.$message.error(msg)
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'name', 'ruleField', 'ruleTable'))
      }

    }
  }
</script>

<style scoped>
</style>