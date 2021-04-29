<template>
  <div>
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal"
                     @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
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
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small"
                    @click="downloadFile(text)">下载
          </a-button>
        </template>
        <span slot="action" slot-scope="text, record">
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
      dataList: {
        type: Array,
        default: () => ([])
      }
    },
    watch: {
      mainData: {
        immediate: true,
        handler(val) {
          if (!this.mainData) {
            this.clearList()
          } else {
            this.loadData(1)
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
      this.dataSource = this.dataList
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
        col.push({
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        })
        return col
      }
    },
    methods: {
      loadData(arg) {
        if (this.isNew) {
          // 新表单
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1
        }
        //查询条件
        let params = this.getQueryParams()
        // 设置外键查询
        this.subForm.foreignKeys.forEach(f => params[f.field] = this.mainData[f.key])
        params.tableType = this.subForm.tableType
        this.loading = true
        this.dataSource = this.dataList
        this.ipagination.total = this.dataList.length
        this.loading = false
      },
      onFormItemInitial({ fieldList, properties }) {
        this.fieldList = fieldList
        // 设置默认值
        if (!this.model.id) {
          this.$nextTick(() => {
            for (let f of this.fieldList) {
              if (properties[f].defVal) {
                this.$set(this.model,f,properties[f].defVal)
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
        fieldList.push({
          type: 'popup',
          value: 'companyCode',
          text: '需方代码',
          popup: { code: 'lg_company', field: 'code', orgFields: 'code', destFields: 'company_code' }
        })
        fieldList.push({ type: 'string', value: 'companyName', text: '需方名称', dictCode: '' })
        fieldList.push({
          type: 'popup',
          value: 'supplierCode',
          text: '供方代码',
          popup: { code: 'lg_company', field: 'code', orgFields: 'code', destFields: 'supplier_code' }
        })
        fieldList.push({ type: 'string', value: 'supplierName', text: '供方名称', dictCode: '' })
        fieldList.push({ type: 'sel_depart', value: 'applyDept', text: '采购部门' })
        fieldList.push({ type: 'string', value: 'amount', text: '中标金额', dictCode: '' })
        fieldList.push({ type: 'string', value: 'programCode', text: '项目编号', dictCode: '' })
        fieldList.push({ type: 'string', value: 'programName', text: '项目名称', dictCode: '' })
        fieldList.push({ type: 'sel_user', value: 'userId', text: '经办人' })
        fieldList.push({ type: 'int', value: 'hasProtocol', text: '是否需要技术协议', dictCode: '' })
        fieldList.push({ type: 'date', value: 'biddingTime', text: '中标时间' })
        fieldList.push({ type: 'int', value: 'attract', text: '招标方式', dictCode: '' })
        fieldList.push({ type: 'int', value: 'hasRate', text: '是否含税', dictCode: '' })
        fieldList.push({ type: 'string', value: 'rate', text: '税率', dictCode: '' })
        fieldList.push({ type: 'string', value: 'biddingCode', text: '招标编号', dictCode: '' })
        fieldList.push({ type: 'int', value: 'type', text: '招标类型', dictCode: 'bidding_type' })
        this.superFieldList = fieldList
      }
    }
  }
</script>

<style scoped>

</style>