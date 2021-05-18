<template>
  <div>
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button v-if="!disabled" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal"
                     @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0&&!disabled">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">
        {{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span v-if="!disabled" slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
              <a>删除</a>
            </a-popconfirm>
        </span>
      </a-table>
    </div>

    <j-modal
      :title="title"
      :width="width"
      :visible="visible"
      :confirmLoading="confirmLoading"
      force-render
      switchFullscreen
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭">
      <a-spin :spinning="confirmLoading">
        <DefaultActFormItem ref="form" :model="model" :isNew="!model.id" :form-id="subForm.code"
                            @formItemInitial="onFormItemInitial"></DefaultActFormItem>
      </a-spin>
    </j-modal>
  </div>
</template>

<script>
  import DefaultActFormItem from './DefaultActFormItem'
  import { getAction, postAction, httpAction } from '@/api/manage'
  import { randomUUID, filterObj } from '@/utils/util'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'


  export default {
    name: 'DefaultSubListForm',
    mixins: [JeecgListMixin],
    components: { DefaultActFormItem },
    props: {
      subForm: {
        type: Object,
        default: () => ({})
      },
      mainData: {
        type: Object,
        default: () => ({}),
        required: false
      },
      isNew: {
        type: Boolean,
        default: false
      },
      disabled: {
        type: Boolean,
        default: false
      }
    },
    watch: {
      mainData: {
        immediate: true,
        handler(val) {
          if (!this.mainData) {
            this.clearList()
          } else {
            this.loadData()
          }
        }
      }
    },
    data() {
      return {
        model: {},
        title: '操作',
        width: 800,
        visible: false,
        confirmLoading: false,
        superFieldList: [],
        url: {
          list: '/online/cgform/api/getData/',
          delete: '',
          deleteBatch: '',
          exportXlsUrl: '',
          importUrl: '',
          add: '',
          edit: ''
        },
        modalNew: false,
        fieldList: []
      }
    },
    created() {
      this.getSuperFieldList()
    },
    computed: {
      importExcelUrl() {
        return `${window._CONFIG['domianURL']}/${this.url.importUrl}/${this.mainData.id}`
      },
      columns() {
        let col = this.subForm.columns.map(m => ({
          title: m.title,
          align: m.align,
          dataIndex: m.dataIndex,
          sorter: m.sorter
        }))
        if (!this.disabled) {
          col.push({
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            fixed: 'right',
            width: 147,
            scopedSlots: { customRender: 'action' }
          })
        }
        return col
      }
    },
    methods: {
      loadData(arg) {
        if (this.isNew) {
          // 新表单
          return
        }
        //查询条件
        let params = this.getQueryParams()
        // 设置外键查询
        this.subForm.foreignKeys.forEach(f => params[f.field] = this.mainData[f.key])
        params.tableType = this.subForm.tableType
        this.loading = true
        this.dataSource = this.mainData[this.subForm.currentTableName]
        this.loading = false
      },
      onFormItemInitial({ fieldList, properties }) {
        this.fieldList = fieldList
        // 设置默认值
        if (!this.model.id) {
          this.$nextTick(() => {
            for (let f of this.fieldList) {
              if (properties[f].defVal) {
                this.$set(this.model, f, properties[f].type === 'number' ? Number(properties[f].defVal) : properties[f].defVal)
              }
            }
          })
        }
      },
      getFieldList() {
        return this.fieldList
      },
      getDataSource() {
        return this.dataSource
      },
      clearList() {
        this.dataSource = []
        this.selectedRowKeys = []
        this.ipagination.current = 1
      },
      add() {
        this.edit({})
      },
      edit(record) {
        this.model = Object.assign({}, record)
        this.visible = true
      },
      handleAdd() {
        this.modalNew = true
        this.title = '新增'
        this.add()
      },
      handleEdit(record) {
        this.modalNew = false
        this.title = '编辑'
        this.edit(record)
      },
      close() {
        this.$emit('close')
        this.visible = false
        this.$refs.form.clearValidate()
      },
      handleOk() {
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.modalNew) {
              this.model.id = randomUUID()
              this.subForm.foreignKeys.forEach(f => this.model[f.field] = this.mainData[f.key])
              this.dataSource.push(this.model)
            } else {
              let index = this.dataSource.findIndex(m => m.id === this.model.id)
              this.dataSource[index] = this.model
            }
            this.close()
          } else {
            return false
          }
        })
      },
      handleDelete(id) {
        let index = this.dataSource.findIndex(m => m.id === id)
        this.dataSource.splice(index, 1)
      },
      handleCancel() {
        this.close()
      },
      getSuperFieldList() {
        let fieldList = []
        fieldList.push({ type: 'string', value: 'supplierName', text: '供方名称', dictCode: '' })
        this.superFieldList = fieldList
      }
    }
  }
</script>

<style scoped>

</style>