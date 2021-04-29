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
      <a-button type="primary" icon="download" @click="handleExportXls('采购合同基础表')">导出</a-button>
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
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{selectedRowKeys.length }}</a>项
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

        <template slot="firstMember" slot-scope="text,record">
          <div>{{record.firstMemberObj.nameCn}}</div>
        </template>
        <template slot="secondMember" slot-scope="text,record">
          <div>{{record.secondMemberObj.nameCn}}</div>
        </template>
        <template slot="thirdMember" slot-scope="text,record">
          <div>{{record.thirdMemberObj.nameCn}}</div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item v-if="record.status==='3'">
                <a @click="applyYinz(record)">用印申请</a>
              </a-menu-item>
<!--              <a-menu-item>-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a>删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <contract-purchase-modal ref="modalForm" @ok="modalFormOk"></contract-purchase-modal>
    <apply-yinz ref="applyYinz" type-code="stamp"></apply-yinz>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ContractPurchaseModal from './modules/ContractPurchaseModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { filterDictTextByDictCode } from '@/components/dict/JDictSelectUtil'
  import ApplyYinz from '../components/ApplyYinz'

  export default {
    name: 'ContractPurchaseList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      ApplyYinz,
      ContractPurchaseModal,
      JSuperQuery
    },
    data() {
      return {
        description: '采购合同基础表管理页面',
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
            dataIndex: 'name'
          },
          {
            title: '合同编号',
            align: 'center',
            dataIndex: 'code'
          },
          {
            title: '类型',
            align: 'center',
            dataIndex: 'typeName'
          },
          {
            title: '我方',
            align: 'center',
            dataIndex: 'firstMember',
            scopedSlots: { customRender: 'firstMember' }
          },
          {
            title: '乙方',
            align: 'center',
            dataIndex: 'secondMember',
            scopedSlots: { customRender: 'secondMember' }
          },
          {
            title: '丙方',
            align: 'center',
            dataIndex: 'thirdMember',
            scopedSlots: { customRender: 'thirdMember' }
          },
          {
            title: '创建人',
            align: 'center',
            dataIndex: 'userName'
          },
          {
            title: '状态',
            align: 'center',
            dataIndex: 'status',
            customRender: (t, r, index) => {
              return filterDictTextByDictCode('contract_status', t)
            }
          },
          {
            title: '签署方数',
            align: 'center',
            dataIndex: 'memberUse',
            customRender: (t, r, index) => {
              return t === '0' ? '双方签署' : '三方签署'
            }
          },
          {
            title: '创建时间',
            align: 'center',
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
          list: '/contract/contractPurchase/list',
          delete: '/contract/contractPurchase/delete',
          deleteBatch: '/contract/contractPurchase/deleteBatch',
          exportXlsUrl: '/contract/contractPurchase/exportXls',
          importExcelUrl: 'contract/contractPurchase/importExcel'
        },
        dictOptions: {},
        superFieldList: [],
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
      applyYinz(r){
        r.tableName = 'contract_purchase'
        this.$refs.applyYinz.show(r)
      },
      initDictConfig() {
      },
      getSuperFieldList() {
        let fieldList = []
        fieldList.push({ type: 'string', value: 'name', text: '合同名称', dictCode: '' })
        fieldList.push({ type: 'string', value: 'code', text: '合同编号', dictCode: '' })
        fieldList.push({ type: 'string', value: 'typeCode', text: '类型编码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'firstMember', text: '我方', dictCode: '' })
        fieldList.push({ type: 'string', value: 'secondMember', text: '乙方', dictCode: '' })
        fieldList.push({ type: 'string', value: 'thirdMember', text: '丙方', dictCode: '' })
        fieldList.push({ type: 'string', value: 'userId', text: '用户id', dictCode: '' })
        fieldList.push({ type: 'string', value: 'status', text: '状态', dictCode: '' })
        fieldList.push({ type: 'string', value: 'remark', text: '备注', dictCode: '' })
        fieldList.push({ type: 'string', value: 'memberUse', text: '签署方数', dictCode: '' })
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>