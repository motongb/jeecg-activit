<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="名称1">
              <a-input placeholder="请输入名称1" v-model="queryParam.nameCn"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="编码">
              <a-input placeholder="请输入编码" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="组别">
                <j-dict-select-tag v-model="queryParam.groups" dictCode="company_groups"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="属性">
                <j-dict-select-tag v-model="queryParam.attr" dictCode="company_attr"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="类型">
                <j-dict-select-tag v-model="queryParam.type" dictCode="company_type"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="状态">
                <j-dict-select-tag v-model="queryParam.status" placeholder="请输入状态" dictCode="company_status"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="服务范围">
                <a-input placeholder="请输入服务范围" v-model="queryParam.serviceRange"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="行业">
                <a-input placeholder="请输入行业" v-model="queryParam.business"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="国家">
                <a-input placeholder="请输入国家" v-model="queryParam.country"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="存续状态">
                <j-dict-select-tag placeholder="请输入存续状态" v-model="queryParam.liveStatus"
                                   dictCode="company_live_status"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('企业信息/合作方信息')">导出</a-button>
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

    <company-modal ref="modalForm" @ok="modalFormOk"></company-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyModal from './modules/CompanyModal'
  import { filterDictTextByDictCode } from '@comp/dict/JDictSelectUtil'

  export default {
    name: 'CompanyList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      CompanyModal
    },
    data() {
      return {
        description: '企业信息/合作方信息管理页面',
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
            title: '名称1',
            align: 'center',
            dataIndex: 'nameCn'
          },
          {
            title: '名称2',
            align: 'center',
            dataIndex: 'nameEn'
          },
          {
            title: '编码',
            align: 'center',
            dataIndex: 'code'
          },
          {
            title: '级别',
            align: 'center',
            dataIndex: 'level',
            customRender: (text, record, index) => {
              //字典值替换通用方法
              return filterDictTextByDictCode('company_level', text)
            }
          },
          {
            title: '类型',
            align: 'center',
            dataIndex: 'type',
            customRender: (text, record, index) => {
              //字典值替换通用方法
              return filterDictTextByDictCode('company_type', text)
            }
          },
          {
            title: '状态',
            align: 'center',
            dataIndex: 'status',
            customRender: (text, record, index) => {
              //字典值替换通用方法
              return filterDictTextByDictCode('company_status', text)
            }
          },
          // {
          //   title: '黑名单',
          //   align: 'center',
          //   dataIndex: 'black'
          // },
          // {
          //   title: '服务范围',
          //   align: 'center',
          //   dataIndex: 'serviceRange'
          // },
          // {
          //   title: '行业',
          //   align: 'center',
          //   dataIndex: 'business'
          // },
          // {
          //   title: '国家',
          //   align: 'center',
          //   dataIndex: 'country'
          // },
          // {
          //   title: '区域',
          //   align: 'center',
          //   dataIndex: 'region'
          // },
          // {
          //   title: '地址',
          //   align: 'center',
          //   dataIndex: 'address'
          // },
          // {
          //   title: '简介',
          //   align: 'center',
          //   dataIndex: 'context'
          // },
          {
            title: '法人',
            align: 'center',
            dataIndex: 'legal'
          },
          // {
          //   title: '经营范围',
          //   align: 'center',
          //   dataIndex: 'register'
          // },
          {
            title: '信用代码',
            align: 'center',
            dataIndex: 'creditCode'
          },
          // {
          //   title: '注册资本',
          //   align: 'center',
          //   dataIndex: 'capital'
          // },
          // {
          //   title: '注册时间',
          //   align: 'center',
          //   dataIndex: 'registerTime'
          // },
          {
            title: '存续状态',
            align: 'center',
            dataIndex: 'liveStatus',
            customRender: (text, record, index) => {
              //字典值替换通用方法
              return filterDictTextByDictCode('company_live_status', text)
            }
          },
          // {
          //   title: '单位电话',
          //   align: 'center',
          //   dataIndex: 'link'
          // },
          // {
          //   title: '邮箱',
          //   align: 'center',
          //   dataIndex: 'email'
          // },
          // {
          //   title: '传真',
          //   align: 'center',
          //   dataIndex: 'fax'
          // },
          // {
          //   title: '省',
          //   align: 'center',
          //   dataIndex: 'province'
          // },
          // {
          //   title: '市',
          //   align: 'center',
          //   dataIndex: 'city'
          // },
          // {
          //   title: '区(县)',
          //   align: 'center',
          //   dataIndex: 'area'
          // },
          // {
          //   title: '联系人',
          //   align: 'center',
          //   dataIndex: 'contactPerson'
          // },
          // {
          //   title: '联系电话',
          //   align: 'center',
          //   dataIndex: 'contactPhone'
          // },
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
          list: '/contract/company/list',
          delete: '/contract/company/delete',
          deleteBatch: '/contract/company/deleteBatch',
          exportXlsUrl: '/contract/company/exportXls',
          importExcelUrl: 'contract/company/importExcel'

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
        fieldList.push({ type: 'string', value: 'nameCn', text: '名称1', dictCode: '' })
        fieldList.push({ type: 'string', value: 'nameEn', text: '名称2', dictCode: '' })
        fieldList.push({ type: 'string', value: 'code', text: '编码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'level', text: '级别', dictCode: '' })
        fieldList.push({ type: 'string', value: 'type', text: '业务类型', dictCode: '' })
        fieldList.push({ type: 'int', value: 'status', text: '状态', dictCode: '' })
        fieldList.push({ type: 'int', value: 'black', text: '黑名单', dictCode: '' })
        fieldList.push({ type: 'string', value: 'serviceRange', text: '服务范围', dictCode: '' })
        fieldList.push({ type: 'string', value: 'business', text: '行业', dictCode: '' })
        fieldList.push({ type: 'string', value: 'country', text: '国家', dictCode: '' })
        fieldList.push({ type: 'string', value: 'region', text: '区域', dictCode: '' })
        fieldList.push({ type: 'string', value: 'address', text: '地址', dictCode: '' })
        fieldList.push({ type: 'string', value: 'context', text: '简介', dictCode: '' })
        fieldList.push({ type: 'string', value: 'legal', text: '法人', dictCode: '' })
        fieldList.push({ type: 'string', value: 'register', text: '经营范围', dictCode: '' })
        fieldList.push({ type: 'string', value: 'creditCode', text: '信用代码', dictCode: '' })
        fieldList.push({ type: 'string', value: 'capital', text: '注册资本', dictCode: '' })
        fieldList.push({ type: 'date', value: 'registerTime', text: '注册时间' })
        fieldList.push({ type: 'int', value: 'liveStatus', text: '存续状态', dictCode: '' })
        fieldList.push({ type: 'string', value: 'link', text: '单位电话', dictCode: '' })
        fieldList.push({ type: 'string', value: 'email', text: '邮箱', dictCode: '' })
        fieldList.push({ type: 'string', value: 'fax', text: '传真', dictCode: '' })
        fieldList.push({ type: 'string', value: 'province', text: '省', dictCode: '' })
        fieldList.push({ type: 'string', value: 'city', text: '市', dictCode: '' })
        fieldList.push({ type: 'string', value: 'area', text: '区(县)', dictCode: '' })
        fieldList.push({ type: 'string', value: 'contactPerson', text: '联系人', dictCode: '' })
        fieldList.push({ type: 'string', value: 'contactPhone', text: '联系电话', dictCode: '' })
        fieldList.push({ type: 'string', value: 'attr', text: '业务属性', dictCode: '' })
        fieldList.push({ type: 'string', value: 'groups', text: '分组', dictCode: '' })
        fieldList.push({ type: 'string', value: 'postCode', text: '邮编', dictCode: '' })
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>