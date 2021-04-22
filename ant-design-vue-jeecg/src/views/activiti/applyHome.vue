<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="4" :sm="4">
            <a-form-item label="流程名称">
              <a-input-search style="margin-bottom: 10px;margin-right:10px;width: 200px"
                              v-model="queryParam.searchProcessKey" allowClear @search="onSearchProcess"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="4">
            <a-form-item label="是否最新">
              <a-switch checkedChildren="是" unCheckedChildren="否" defaultChecked v-model="queryParam.zx"/>
            </a-form-item>
          </a-col>
          <a-col :md="16" :sm="4">
            <a-button @click="onSearchProcess(queryParam.searchProcessKey)" type="primary">查询</a-button>
            <a-button @click="onSearchProcess('')" style="margin-left: 8px">重置</a-button>
            <a-button @click="handleToApplyList" type="primary" style="float: right">前往我的申请列表</a-button>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <a-empty description="无流程可供选择" v-if="activeKeyAll.length==0"/>
    <div v-else>
      <a-collapse v-model="activeKey">
        <a-collapse-panel v-for="(value, index)  in activeKeyAll" :header="filterDictText(dictOptions,value)||'未分类'"
                          :key="value">
          <a-list :grid="{ gutter: 10,column:4}" :dataSource="processDataMap[value]">
            <a-list-item slot="renderItem" slot-scope="item">
              <a-card>
                <div slot="title">
                  <a-row>
                    <a-col span="12" :title="item.name">{{item.name}}</a-col>
                    <a-col span="12" style="text-align: right;">
                      <a href="javascript:void (0)" @click="chooseProcess(item)">发起申请</a>
                    </a-col>
                  </a-row>
                </div>
                <b>版本：</b>v.{{item.version}}
                <br/>
                <b>说明：</b>{{item.description}}
              </a-card>
            </a-list-item>
          </a-list>
        </a-collapse-panel>
      </a-collapse>
    </div>
  </a-card>

</template>

<script>
  import { activitiMixin } from '@/views/activiti/mixins/activitiMixin'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import JTreeSelect from '@/components/jeecg/JTreeSelect'
  import { initDictOptions, filterDictText } from '@/components/dict/JDictSelectUtil'
  import historicDetail from '@/views/activiti/historicDetail'
  import activitiSetting from './mixins/activitiSetting'
  import { getAction } from '@/api/manage'
  import { setStore } from '@/utils/storage'
  import moment from 'moment'

  export default {
    name: 'applyHome',
    mixins: [activitiMixin],
    components: {
      JEllipsis
      , JTreeSelect
      , historicDetail
    },
    data() {
      return {
        description: '所有',
        dictOptions: [],
        url: {
          getProcessDataList: '/activiti_process/listData',
          getFirstNode: '/actProcessIns/getFirstNode',
          applyBusiness: '/actBusiness/draft'
        },
        // 查询条件
        queryParam: {
          zx: true, // 是否最新
          searchProcessKey: '' // 名称
        },
        // 表头
        labelCol: {
          xs: { span: 4 },
          sm: { span: 4 }
        },
        wrapperCol: {
          xs: { span: 20 },
          sm: { span: 20 }
        },
        processModalVisible: null,
        activeKeyAll: [],
        activeKey: [],
        processDataMap: {},
        addApplyLoading: false
      }
    },
    computed: {},
    mounted() {
      this.initDictConfig()
      this.getProcessList()
    },
    methods: {
      initDictConfig() {
        //初始化字典 - 流程分类
        initDictOptions('bpm_process_type').then((res) => {
          if (res.success) {
            this.dictOptions = res.result
          }
        })
      },
      filterDictText(dictOptions, text) {
        if (dictOptions instanceof Array) {
          for (let dictItem of dictOptions) {
            if (text === dictItem.value) {
              return dictItem.text
            }
          }
        }
        return text || text == 'null' ? '' : text
      },
      /*加载流程列表*/
      getProcessList() {
        this.addApplyLoading = true
        getAction(this.url.getProcessDataList, { status: 1, roles: true, zx: this.queryParam.zx }).then(res => {
          this.activeKeyAll = []
          if (res.success) {
            let result = res.result.records || []
            if (result.length > 0) {
              let searchProcessKey = this.queryParam.searchProcessKey
              if (searchProcessKey) { //过滤条件
                result = _.filter(result, function(o) {
                  return o.name.indexOf(searchProcessKey) > -1
                })
              }
              this.processDataMap = _.groupBy(result, 'categoryId')
              for (const categoryId in this.processDataMap) {
                this.activeKeyAll.push(categoryId)
              }
              this.activeKey = this.activeKeyAll
            }
            this.processModalVisible = true
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => this.addApplyLoading = false)
      },
      onSearchProcess(value) {
        this.queryParam.searchProcessKey = value
        this.getProcessList()
      },
      chooseProcess(v) {
        if (!v.routeName) {
          this.$message.warning(
            '该流程信息未配置表单，请联系开发人员！'
          )
          return
        }
        let lcModa = {}
        lcModa.title = moment().format('YYYY-MM-DD HH:mm:ss') + v.name
        lcModa.isNew = true
        lcModa.processData = v
        lcModa.from = activitiSetting.applyHomePath
        lcModa.reload = true
        setStore('lcModa', lcModa)
        this.$router.push(activitiSetting.applyFormPath)
      },
      /*前往我的申请页面*/
      handleToApplyList() {
        this.$router.push(activitiSetting.applyListPath)
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>