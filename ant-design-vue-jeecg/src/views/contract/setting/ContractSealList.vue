<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="印章名称">
              <a-input placeholder="请输入印章名称" v-model="queryParam.name"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('合同印章')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" @click="previewImage = getImgView(text),previewVisible=true "
               style="max-width:80px;font-size: 12px;font-style: italic;cursor: pointer"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
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
    <a-modal :visible="previewVisible" :footer="null" @cancel="previewVisible=false">
      <img alt="example" style="width: 100%" :src="previewImage"/>
    </a-modal>
    <contract-seal-modal ref="modalForm" @ok="modalFormOk"></contract-seal-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ContractSealModal from './modules/ContractSealModal'

  export default {
    name: 'ContractSealList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      ContractSealModal
    },
    data() {
      return {
        description: '合同印章管理页面',
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
            title: '印章名称',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '印章编号',
            align: 'center',
            dataIndex: 'sealCode'
          },
          {
            title: '规格',
            align: 'center',
            dataIndex: 'format'
          },
          {
            title: '用印数',
            align: 'center',
            dataIndex: 'useNum'
          },
          {
            title: '使用人',
            align: 'center',
            dataIndex: 'userNames'
          },
          {
            title: '状态',
            align: 'center',
            dataIndex: 'status',
            customRender: function(t, r, index) {
              return t==='0'?'启用':'禁用'
            }
          },
          {
            title: '印章',
            align: 'center',
            dataIndex: 'fileUrl',
            scopedSlots: { customRender: 'imgSlot' }
          },
          {
            title: '所属',
            align: 'center',
            dataIndex: 'belongName'
          },
          {
            title: '防伪码',
            align: 'center',
            dataIndex: 'fakeCode'
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
          list: '/contract/contractSeal/list',
          delete: '/contract/contractSeal/delete',
          deleteBatch: '/contract/contractSeal/deleteBatch',
          exportXlsUrl: '/contract/contractSeal/exportXls',
          importExcelUrl: 'contract/contractSeal/importExcel'

        },
        dictOptions: {},
        superFieldList: [],
        previewVisible: false,
        previewImage: ''
      }
    },
    created() {
      this.getSuperFieldList()
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      initDictConfig() {
      },
      getSuperFieldList() {
        let fieldList = []
        fieldList.push({ type: 'string', value: 'name', text: '印章名称', dictCode: '' })
        fieldList.push({ type: 'string', value: 'sealCode', text: '印章编号', dictCode: '' })
        fieldList.push({ type: 'string', value: 'format', text: '规格', dictCode: '' })
        fieldList.push({ type: 'int', value: 'useNum', text: '用印文件', dictCode: '' })
        fieldList.push({ type: 'string', value: 'users', text: '使用人', dictCode: '' })
        fieldList.push({ type: 'string', value: 'status', text: '状态', dictCode: '' })
        fieldList.push({ type: 'string', value: 'fileUrl', text: '印章文件', dictCode: '' })
        fieldList.push({ type: 'string', value: 'belong', text: '所属', dictCode: '' })
        fieldList.push({ type: 'string', value: 'fakeCode', text: '防伪码', dictCode: '' })
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>