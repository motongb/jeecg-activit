<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="模板名称">
              <a-input placeholder="请输入模板名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="编码">
              <a-input placeholder="请输入编码" v-model="queryParam.modelKey"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('合同模板')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑信息</a>
          <a-divider type="vertical"/>
          <a @click="handleEditDoc(record)">编辑模板</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="setParams(record)">配置</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <contract-model-modal ref="modalForm" @ok="modalFormOk"></contract-model-modal>
    <!--配置区域-->
    <a-drawer
      title="配置参数"
      :width="width"
      placement="right"
      :closable="false"
      @close="handleCancel"
      :visible="visible">
      <div>
        <a-select v-model="currentSelected" style="width: 100%" @change="handleChange">
          <a-select-option value="-1">请选择</a-select-option>
          <template v-for="item in paramsFieldList">
            <a-select-option :value="item.id">{{item.name}}</a-select-option>
          </template>
        </a-select>
        <p style="margin-top: 10px;font-size: 10px;color: grey">提示：模板中有表格，需要设置列表行的列表索引，即表格的序号，序号从0开始</p>
        <a-table rowKey="id" :columns="paramsColumns" :data-source="paramsData">
          <template slot="tableIndex" slot-scope="text, record">
            <editable-cell :text="record.tableIndex" @change="onCellChange(record,$event)"/>
          </template>
        </a-table>
      </div>
      <div class="drawer-footer">
        <a-button type="primary" @click="handleOk" style="margin-bottom: 0;margin-right: 20px">保存</a-button>
        <a-button @click="handleCancel" style="margin-bottom: 0;">关闭</a-button>
      </div>
    </a-drawer>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ContractModelModal from './modules/ContractModelModal'
  import { getAction, putAction } from '@/api/manage'
  import { filterDictTextByDictCode } from '@comp/dict/JDictSelectUtil'
  import EditableCell from '../components/EditableCell'

  export default {
    name: 'ContractModelList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      ContractModelModal,
      EditableCell
    },
    data() {
      return {
        description: '合同模板管理页面',
        paramsColumns: [
          {
            title: '字段名称',
            align: 'left',
            dataIndex: 'name'
          },
          {
            title: '字段key',
            align: 'left',
            dataIndex: 'fieldKey'
          },
          {
            title: '类型',
            align: 'left',
            dataIndex: 'type',
            customRender: function(t, r, index) {
              return filterDictTextByDictCode('contract_field_type', t)
            }
          },
          {
            title: '列表索引',
            align: 'left',
            scopedSlots: { customRender: 'tableIndex' }
          }
        ],
        // 表头
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
            title: '模板名称',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '模板文件id',
            align: 'center',
            dataIndex: 'fileId'
          },
          {
            title: '编码',
            align: 'center',
            dataIndex: 'modelKey'
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
        url: {
          list: '/contract/contractModel/list',
          edit: '/contract/contractModel/edit',
          delete: '/contract/contractModel/delete',
          deleteBatch: '/contract/contractModel/deleteBatch',
          exportXlsUrl: '/contract/contractModel/exportXls',
          importExcelUrl: 'contract/contractModel/importExcel',
          paramsFieldTree: '/contract/contractFieldParams/tree'
        },
        visible: false,
        width: 800,
        dictOptions: {},
        superFieldList: [],
        paramsFieldList: [],
        editRecord: {},
        currentSelected: '',
        paramsData: []
      }
    },
    created() {
      this.getSuperFieldList()
      this.getParamsFieldList()
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      onCellChange(record, value) {
        record.tableIndex = value
        console.log(this.paramsData)
      },
      getParamsFieldList() {
        getAction(this.url.paramsFieldTree).then(res => {
          this.paramsFieldList = res.result
        })
      },
      handleChange(value) {
        const item = this.paramsFieldList.find(m => m.id === value)
        if (item) {
          this.paramsData = item.children
        } else {
          this.paramsData = []
        }
      },
      handleOk() {
        putAction(this.url.edit, {
          id: this.editRecord.id,
          fieldId: this.currentSelected,
          paramsFields: JSON.stringify(this.paramsData)
        }).then(res => {
          this.$message.success(res.message)
          this.handleCancel()
        })
      },
      handleCancel() {
        this.visible = false
      },
      handleEditDoc(record) {
        this.$router.push({ path: '/contract/setting/modelPage', query: { id: record.id, fileId: record.fileId } })
      },
      initDictConfig() {
      },
      getSuperFieldList() {
        let fieldList = []
        fieldList.push({ type: 'string', value: 'name', text: '模板名称', dictCode: '' })
        fieldList.push({ type: 'string', value: 'fileId', text: '模板文件id', dictCode: '' })
        fieldList.push({ type: 'string', value: 'modelKey', text: '编码', dictCode: '' })
        this.superFieldList = fieldList
      },
      setParams(record) {
        this.visible = true
        this.currentSelected = record.fieldId
        this.editRecord = record
        if (record.paramsFields) {
          this.paramsData = JSON.parse(record.paramsFields)
          console.log(this.paramsData)
        }
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';

  .drawer-footer {
    position: absolute;
    bottom: -8px;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>