<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="公司名称">
              <a-input placeholder="请输入公司名称" v-model="queryParam.companyName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="供应商名称">
              <a-input placeholder="请输入供应商名称" v-model="queryParam.supplierName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="采购组织">
                <a-input placeholder="请输入采购组织" v-model="queryParam.applyDept"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="项目编号">
                <a-input placeholder="请输入项目编号" v-model="queryParam.programCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="项目名称">
                <a-input placeholder="请输入项目名称" v-model="queryParam.programName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="招标编号">
                <a-input placeholder="请输入招标编号" v-model="queryParam.postCode"></a-input>
              </a-form-item>
            </a-col>
          </template>
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
      <a-button type="primary" icon="download" @click="handleExportXls('柳钢中标信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
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
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type:'radio'}"
        :customRow="clickThenSelect"
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
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <a-tabs defaultActiveKey="1">
      <a-tab-pane tab="柳钢合同中标项" key="1" >
        <LgContractBiddingItemList :mainId="selectedMainId" />
      </a-tab-pane>
    </a-tabs>

    <lgContractBidding-modal ref="modalForm" @ok="modalFormOk"></lgContractBidding-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import LgContractBiddingModal from './modules/LgContractBiddingModal'
  import { getAction } from '@/api/manage'
  import LgContractBiddingItemList from './LgContractBiddingItemList'
  import '@/assets/less/TableExpand.less'

  export default {
    name: "LgContractBiddingList",
    mixins:[JeecgListMixin],
    components: {
      LgContractBiddingItemList,
      LgContractBiddingModal
    },
    data () {
      return {
        description: '柳钢中标信息管理页面',
        // 表头
        columns: [
          {
            title:'公司代码',
            align:"center",
            dataIndex: 'companyCode'
          },
          {
            title:'公司名称',
            align:"center",
            dataIndex: 'companyName'
          },
          {
            title:'供应商代码',
            align:"center",
            dataIndex: 'supplierCode'
          },
          {
            title:'供应商名称',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title:'采购组织',
            align:"center",
            dataIndex: 'applyDept'
          },
          {
            title:'中标金额',
            align:"center",
            dataIndex: 'amount'
          },
          {
            title:'项目编号',
            align:"center",
            dataIndex: 'programCode'
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'programName'
          },
          {
            title:'经办人',
            align:"center",
            dataIndex: 'userId'
          },
          {
            title:'是否需要技术协议',
            align:"center",
            dataIndex: 'isProtocol'
          },
          {
            title:'中标时间',
            align:"center",
            dataIndex: 'biddingTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'属性',
            align:"center",
            dataIndex: 'attract'
          },
          {
            title:'是否含税',
            align:"center",
            dataIndex: 'isRate'
          },
          {
            title:'税率',
            align:"center",
            dataIndex: 'rate'
          },
          {
            title:'招标编号',
            align:"center",
            dataIndex: 'postCode'
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
          list: "/contract/lgContractBidding/list",
          delete: "/contract/lgContractBidding/delete",
          deleteBatch: "/contract/lgContractBidding/deleteBatch",
          exportXlsUrl: "/contract/lgContractBidding/exportXls",
          importExcelUrl: "contract/lgContractBidding/importExcel",
        },
        dictOptions:{
        },
        /* 分页参数 */
        ipagination:{
          current: 1,
          pageSize: 5,
          pageSizeOptions: ['5', '10', '50'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        selectedMainId:'',
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      clickThenSelect(record) {
        return {
          on: {
            click: () => {
              this.onSelectChange(record.id.split(","), [record]);
            }
          }
        }
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.selectedMainId=''
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedMainId=selectedRowKeys[0]
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        this.onClearSelected()
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
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