<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.name"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('合同用印记录表')">导出</a-button>
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

        <template slot="stamp" slot-scope="text, record">
          <div style="display: inline" v-for="(item,i) in record.sealVos">
            {{`${item.name}${item.num}份`}}
            <div style="display: inline" v-if="i<record.sealVos.length-1"> |</div>
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleDetail(record)">详情</a>
<!--          <a-divider type="vertical"/>-->
<!--          <a-dropdown>-->
<!--            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>-->
<!--            <a-menu slot="overlay">-->
<!--              <a-menu-item>-->
<!--                <a @click="handleDetail(record)">详情</a>-->
<!--              </a-menu-item>-->
<!--              <a-menu-item>-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a>删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
<!--            </a-menu>-->
<!--          </a-dropdown>-->
        </span>
      </a-table>
    </div>

    <contract-stamp-modal ref="modalForm" @ok="modalFormOk"></contract-stamp-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ContractStampModal from './modules/ContractStampModal'
  import { filterDictTextByDictCode } from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'ContractStampList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      ContractStampModal
    },
    data() {
      return {
        description: '合同用印记录表管理页面',
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
            title: '合同名称',
            align: 'center',
            dataIndex: 'contractName'
          },
          {
            title: '合同编号',
            align: 'center',
            dataIndex: 'contractCode'
          },
          {
            title: '状态',
            align: 'center',
            dataIndex: 'status',
            customRender: (t, r, index) => {
              return filterDictTextByDictCode('contract_stamp_status', t)
            }
          },
          {
            title: '类型编码',
            align: 'center',
            dataIndex: 'typeCode'
          },
          {
            title: '备注',
            align: 'center',
            dataIndex: 'remark'
          },
          {
            title: '印章',
            align: 'center',
            dataIndex: 'stamp',
            scopedSlots: { customRender: 'stamp' }
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'userName'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime'
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
          list: '/contract/contractStamp/list',
          delete: '/contract/contractStamp/delete',
          deleteBatch: '/contract/contractStamp/deleteBatch',
          exportXlsUrl: '/contract/contractStamp/exportXls',
          importExcelUrl: 'contract/contractStamp/importExcel'

        },
        dictOptions: {},
        superFieldList: []
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
        fieldList.push({ type: 'string', value: 'tableId', text: '数据id', dictCode: '' })
        fieldList.push({ type: 'string', value: 'tableName', text: '表名', dictCode: '' })
        fieldList.push({ type: 'string', value: 'status', text: '状态', dictCode: '' })
        fieldList.push({ type: 'string', value: 'typeCode', text: '类型编码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'remark', text: '备注', dictCode: '' })
        fieldList.push({ type: 'string', value: 'stampType', text: '印章类型', dictCode: '' })
        fieldList.push({ type: 'string', value: 'stampNum', text: '印章份数', dictCode: '' })
        fieldList.push({ type: 'string', value: 'fileIds', text: '盖章文件', dictCode: '' })
        fieldList.push({ type: 'string', value: 'contractName', text: '合同名称', dictCode: '' })
        fieldList.push({ type: 'string', value: 'contractCode', text: '合同编号', dictCode: '' })
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>