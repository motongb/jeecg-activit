<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('柳钢采购合同表')">导出</a-button>
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
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
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

    <lg-contract-purchase-modal ref="modalForm" @ok="modalFormOk"></lg-contract-purchase-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import LgContractPurchaseModal from './modules/LgContractPurchaseModal'

  export default {
    name: 'LgContractPurchaseList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      LgContractPurchaseModal
    },
    data() {
      return {
        description: '柳钢采购合同表管理页面',
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
            title: '类型编码',
            align: 'center',
            dataIndex: 'typeCode'
          },
          {
            title: '我方编码',
            align: 'center',
            dataIndex: 'firstMember'
          },
          {
            title: '乙方编码',
            align: 'center',
            dataIndex: 'secondMember'
          },
          {
            title: '丙方编码',
            align: 'center',
            dataIndex: 'thirdMember'
          },
          {
            title: '经办人',
            align: 'center',
            dataIndex: 'userId'
          },
          {
            title: '状态',
            align: 'center',
            dataIndex: 'status'
          },
          {
            title: '备注',
            align: 'center',
            dataIndex: 'remark'
          },
          {
            title: '签署方数',
            align: 'center',
            dataIndex: 'memberUse'
          },
          {
            title: '是否自动编号,1-是,0-否',
            align: 'center',
            dataIndex: 'isAutoCode'
          },
          {
            title: '流程记录id',
            align: 'center',
            dataIndex: 'actBusiness'
          },
          {
            title: '公司代码',
            align: 'center',
            dataIndex: 'companyCode'
          },
          {
            title: '采购方式',
            align: 'center',
            dataIndex: 'purchaseWay'
          },
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'programNo'
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'programName'
          },
          {
            title: '招标编号',
            align: 'center',
            dataIndex: 'postCode'
          },
          {
            title: '中标id',
            align: 'center',
            dataIndex: 'biddingId'
          },
          {
            title: '合同性质',
            align: 'center',
            dataIndex: 'attr'
          },
          {
            title: '年度',
            align: 'center',
            dataIndex: 'year'
          },
          {
            title: '是否使用模板：1-是，0-否',
            align: 'center',
            dataIndex: 'useModel'
          },
          {
            title: '原模版id',
            align: 'center',
            dataIndex: 'sourceModel'
          },
          {
            title: '合同正文',
            align: 'center',
            dataIndex: 'fileContract'
          },
          {
            title: '模板文件',
            align: 'center',
            dataIndex: 'fileModel'
          },
          {
            title: '合同附件',
            align: 'center',
            dataIndex: 'fileAttach'
          },
          {
            title: '开始时间',
            align: 'center',
            dataIndex: 'startTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '结束时间',
            align: 'center',
            dataIndex: 'endTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '合同价款',
            align: 'center',
            dataIndex: 'amount'
          },
          {
            title: '让利',
            align: 'center',
            dataIndex: 'jangli'
          },
          {
            title: '货币码',
            align: 'center',
            dataIndex: 'coin'
          },
          {
            title: '付款方式',
            align: 'center',
            dataIndex: 'payWay'
          },
          {
            title: '是否含税',
            align: 'center',
            dataIndex: 'isRate'
          },
          {
            title: '税率',
            align: 'center',
            dataIndex: 'rate'
          },
          {
            title: '是否安装',
            align: 'center',
            dataIndex: 'isInstall'
          },
          {
            title: '技术协议',
            align: 'center',
            dataIndex: 'protocol'
          },
          {
            title: '签订地点',
            align: 'center',
            dataIndex: 'signPlace'
          },
          {
            title: '签订时间',
            align: 'center',
            dataIndex: 'signTime',
            customRender: function(text) {
              return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
            }
          },
          {
            title: '联系方式',
            align: 'center',
            dataIndex: 'link'
          },
          {
            title: '有无技术协议',
            align: 'center',
            dataIndex: 'hasProtocol'
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
          list: '/contract/lgContractPurchase/list',
          delete: '/contract/lgContractPurchase/delete',
          deleteBatch: '/contract/lgContractPurchase/deleteBatch',
          exportXlsUrl: '/contract/lgContractPurchase/exportXls',
          importExcelUrl: 'contract/lgContractPurchase/importExcel'

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
        fieldList.push({ type: 'string', value: 'name', text: '合同名称', dictCode: '' })
        fieldList.push({ type: 'string', value: 'code', text: '合同编号', dictCode: '' })
        fieldList.push({ type: 'string', value: 'typeCode', text: '类型编码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'firstMember', text: '我方编码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'secondMember', text: '乙方编码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'thirdMember', text: '丙方编码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'userId', text: '经办人', dictCode: '' })
        fieldList.push({ type: 'int', value: 'status', text: '状态', dictCode: '' })
        fieldList.push({ type: 'string', value: 'remark', text: '备注', dictCode: '' })
        fieldList.push({ type: 'int', value: 'memberUse', text: '签署方数', dictCode: '' })
        fieldList.push({ type: 'int', value: 'isAutoCode', text: '是否自动编号,1-是,0-否', dictCode: '' })
        fieldList.push({ type: 'string', value: 'actBusiness', text: '流程记录id', dictCode: '' })
        fieldList.push({ type: 'string', value: 'companyCode', text: '公司代码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'purchaseWay', text: '采购方式', dictCode: '' })
        fieldList.push({ type: 'string', value: 'programNo', text: '项目编号', dictCode: '' })
        fieldList.push({ type: 'string', value: 'programName', text: '项目名称', dictCode: '' })
        fieldList.push({ type: 'string', value: 'postCode', text: '招标编号', dictCode: '' })
        fieldList.push({ type: 'string', value: 'biddingId', text: '中标id', dictCode: '' })
        fieldList.push({ type: 'string', value: 'attr', text: '合同性质', dictCode: '' })
        fieldList.push({ type: 'string', value: 'year', text: '年度', dictCode: '' })
        fieldList.push({ type: 'int', value: 'useModel', text: '是否使用模板：1-是，0-否', dictCode: '' })
        fieldList.push({ type: 'string', value: 'sourceModel', text: '原模版id', dictCode: '' })
        fieldList.push({ type: 'string', value: 'fileContract', text: '合同正文', dictCode: '' })
        fieldList.push({ type: 'string', value: 'fileModel', text: '模板文件', dictCode: '' })
        fieldList.push({ type: 'string', value: 'fileAttach', text: '合同附件', dictCode: '' })
        fieldList.push({ type: 'date', value: 'startTime', text: '开始时间' })
        fieldList.push({ type: 'date', value: 'endTime', text: '结束时间' })
        fieldList.push({ type: 'string', value: 'amount', text: '合同价款', dictCode: '' })
        fieldList.push({ type: 'string', value: 'jangli', text: '让利', dictCode: '' })
        fieldList.push({ type: 'string', value: 'coin', text: '货币码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'payWay', text: '付款方式', dictCode: '' })
        fieldList.push({ type: 'int', value: 'isRate', text: '是否含税', dictCode: '' })
        fieldList.push({ type: 'string', value: 'rate', text: '税率', dictCode: '' })
        fieldList.push({ type: 'int', value: 'isInstall', text: '是否安装', dictCode: '' })
        fieldList.push({ type: 'string', value: 'protocol', text: '技术协议', dictCode: '' })
        fieldList.push({ type: 'string', value: 'signPlace', text: '签订地点', dictCode: '' })
        fieldList.push({ type: 'date', value: 'signTime', text: '签订时间' })
        fieldList.push({ type: 'string', value: 'link', text: '联系方式', dictCode: '' })
        fieldList.push({ type: 'int', value: 'hasProtocol', text: '有无技术协议', dictCode: '' })
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>