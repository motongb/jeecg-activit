<template>
  <a-card :bordered="false" :class="'cust-erp-sub-tab'">
    <!-- 操作按钮区域 -->
    <div class="table-operator" v-if="mainId">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('柳钢合同中标项')">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel">
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
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

    <lgContractBiddingItem-modal ref="modalForm" @ok="modalFormOk" :mainId="mainId"></lgContractBiddingItem-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import LgContractBiddingItemModal from './modules/LgContractBiddingItemModal'

  export default {
    name: "LgContractBiddingItemList",
    mixins:[JeecgListMixin],
    components: { LgContractBiddingItemModal },
    props:{
      mainId:{
        type:String,
        default:'',
        required:false
      }
    },
    watch:{
      mainId:{
        immediate: true,
        handler(val) {
          if(!this.mainId){
            this.clearList()
          }else{
            this.queryParam['biddingId'] = val
            this.loadData(1);
          }
        }
      }
    },
    data () {
      return {
        description: '柳钢中标信息管理页面',
        disableMixinCreated:true,
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
            title:'采购计划编号',
            align:"center",
            dataIndex: 'planId'
          },
          {
            title:'物料名称',
            align:"center",
            dataIndex: 'itemName'
          },
          {
            title:'物料编码',
            align:"center",
            dataIndex: 'itemCode'
          },
          {
            title:'计量单位',
            align:"center",
            dataIndex: 'unit'
          },
          {
            title:'物料组',
            align:"center",
            dataIndex: 'groups'
          },
          {
            title:'数量',
            align:"center",
            dataIndex: 'count'
          },
          {
            title:'单价',
            align:"center",
            dataIndex: 'cost'
          },
          {
            title:'金额',
            align:"center",
            dataIndex: 'amount'
          },
          {
            title:'价格单位',
            align:"center",
            dataIndex: 'costUnit'
          },
          {
            title:'交货期',
            align:"center",
            dataIndex: 'deadline',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'订货依据及参数要求',
            align:"center",
            dataIndex: 'standard'
          },
          {
            title:'品牌',
            align:"center",
            dataIndex: 'brand'
          },
          {
            title:'申报单位',
            align:"center",
            dataIndex: 'applyDept'
          },
          {
            title:'申报人',
            align:"center",
            dataIndex: 'applyPerson'
          },
          {
            title:'需求工厂',
            align:"center",
            dataIndex: 'factory'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/contract/lgContractBidding/listLgContractBiddingItemByMainId",
          delete: "/contract/lgContractBidding/deleteLgContractBiddingItem",
          deleteBatch: "/contract/lgContractBidding/deleteBatchLgContractBiddingItem",
          exportXlsUrl: "/contract/lgContractBidding/exportLgContractBiddingItem",
          importUrl: "/contract/lgContractBidding/importLgContractBiddingItem",
        },
        dictOptions:{
        },
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
    },
    computed: {
      importExcelUrl(){
        return `${window._CONFIG['domianURL']}/${this.url.importUrl}/${this.mainId}`;
      }
    },
    methods: {
      clearList(){
        this.dataSource=[]
        this.selectedRowKeys=[]
        this.ipagination.current = 1
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'companyCode',text:'公司代码',dictCode:''})
        fieldList.push({type:'string',value:'companyName',text:'公司名称',dictCode:''})
        fieldList.push({type:'string',value:'supplierCode',text:'供应商代码',dictCode:''})
        fieldList.push({type:'string',value:'supplierName',text:'供应商名称',dictCode:''})
        fieldList.push({type:'string',value:'applyDept',text:'采购组织',dictCode:''})
        fieldList.push({type:'string',value:'amount',text:'中标金额',dictCode:''})
        fieldList.push({type:'string',value:'programCode',text:'项目编号',dictCode:''})
        fieldList.push({type:'string',value:'programName',text:'项目名称',dictCode:''})
        fieldList.push({type:'string',value:'userId',text:'经办人',dictCode:''})
        fieldList.push({type:'string',value:'isProtocol',text:'是否需要技术协议',dictCode:''})
        fieldList.push({type:'date',value:'biddingTime',text:'中标时间'})
        fieldList.push({type:'string',value:'attract',text:'属性',dictCode:''})
        fieldList.push({type:'string',value:'isRate',text:'是否含税',dictCode:''})
        fieldList.push({type:'string',value:'rate',text:'税率',dictCode:''})
        fieldList.push({type:'string',value:'postCode',text:'招标编号',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>
