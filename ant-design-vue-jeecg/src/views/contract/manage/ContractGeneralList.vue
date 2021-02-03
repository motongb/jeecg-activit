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
      <a-button type="primary" icon="download" @click="handleExportXls('一般采购合同')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
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

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
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

    <contract-general-modal ref="modalForm" @ok="modalFormOk"></contract-general-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ContractGeneralModal from './modules/ContractGeneralModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'ContractGeneralList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ContractGeneralModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '一般采购合同管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'项目相关合同',
            align:"center",
            dataIndex: 'relateProject'
          },
          {
            title:'相关项目',
            align:"center",
            dataIndex: 'project'
          },
          {
            title:'是否子合同',
            align:"center",
            dataIndex: 'isSub'
          },
          {
            title:'主合同id',
            align:"center",
            dataIndex: 'parentId'
          },
          {
            title:'预算相关合同',
            align:"center",
            dataIndex: 'budget'
          },
          {
            title:'成本中心',
            align:"center",
            dataIndex: 'costCenter'
          },
          {
            title:'密级',
            align:"center",
            dataIndex: 'isSecret'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'startTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'结束时间',
            align:"center",
            dataIndex: 'endTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'采购方式',
            align:"center",
            dataIndex: 'purchaseType'
          },
          {
            title:'招标编号',
            align:"center",
            dataIndex: 'biddingId'
          },
          {
            title:'相关框架协议',
            align:"center",
            dataIndex: 'protocol'
          },
          {
            title:'采购订单号',
            align:"center",
            dataIndex: 'purchaseId'
          },
          {
            title:'合同金额',
            align:"center",
            dataIndex: 'amount'
          },
          {
            title:'合同金额大写',
            align:"center",
            dataIndex: 'amountLarge'
          },
          {
            title:'模板id',
            align:"center",
            dataIndex: 'modelId'
          },
          {
            title:'合同影像文件',
            align:"center",
            dataIndex: 'filePdf'
          },
          {
            title:'附件',
            align:"center",
            dataIndex: 'fileAttach'
          },
          {
            title:'合同文件',
            align:"center",
            dataIndex: 'fileContract'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/contract/contractGeneral/list",
          delete: "/contract/contractGeneral/delete",
          deleteBatch: "/contract/contractGeneral/deleteBatch",
          exportXlsUrl: "/contract/contractGeneral/exportXls",
          importExcelUrl: "contract/contractGeneral/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'int',value:'relateProject',text:'项目相关合同',dictCode:''})
        fieldList.push({type:'string',value:'project',text:'相关项目',dictCode:''})
        fieldList.push({type:'int',value:'isSub',text:'是否子合同',dictCode:''})
        fieldList.push({type:'string',value:'parentId',text:'主合同id',dictCode:''})
        fieldList.push({type:'int',value:'budget',text:'预算相关合同',dictCode:''})
        fieldList.push({type:'string',value:'costCenter',text:'成本中心',dictCode:''})
        fieldList.push({type:'int',value:'isSecret',text:'密级',dictCode:''})
        fieldList.push({type:'date',value:'startTime',text:'开始时间'})
        fieldList.push({type:'date',value:'endTime',text:'结束时间'})
        fieldList.push({type:'int',value:'purchaseType',text:'采购方式',dictCode:''})
        fieldList.push({type:'string',value:'biddingId',text:'招标编号',dictCode:''})
        fieldList.push({type:'string',value:'protocol',text:'相关框架协议',dictCode:''})
        fieldList.push({type:'string',value:'purchaseId',text:'采购订单号',dictCode:''})
        fieldList.push({type:'string',value:'amount',text:'合同金额',dictCode:''})
        fieldList.push({type:'string',value:'amountLarge',text:'合同金额大写',dictCode:''})
        fieldList.push({type:'string',value:'modelId',text:'模板id',dictCode:''})
        fieldList.push({type:'string',value:'filePdf',text:'合同影像文件',dictCode:''})
        fieldList.push({type:'string',value:'fileAttach',text:'附件',dictCode:''})
        fieldList.push({type:'string',value:'fileContract',text:'合同文件',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>