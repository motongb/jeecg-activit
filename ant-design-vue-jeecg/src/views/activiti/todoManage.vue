<style lang="less">
  @import '~@assets/less/common.less';
</style>
<template>
  <div class="search">
    <a-card>
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="handleSearch">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="任务名称" prop="name">
                <a-input
                  type="text" allowClear
                  v-model="searchForm.name"
                  placeholder="请输入"
                />
              </a-form-item>
            </a-col>
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="12">
                <a-button type="primary" style="left: 10px" @click="handleSearch" icon="search">查询</a-button>
                <a-button type="primary" @click="handleReset" icon="reload"
                          style="margin-left: 8px;left: 10px">重置</a-button>
            </a-col>
          </span>
          </a-row>
        </a-form>
      </div>
      <!--<a-row class="operation">
        <a-button @click="passAll" icon="md-checkmark-circle-outline">批量通过</a-button>
        <a-button @click="backAll" icon="md-close">批量驳回</a-button>
        <a-button @click="getDataList" icon="md-refresh">刷新</a-button>
      </a-row>-->
      <a-row>
        <a-table bordered
                 :loading="loading" rowKey="id"
                 :dataSource="data"
                 :pagination="ipagination" @change="handleTableChange"
                 ref="table">
          <a-table-column title="#" :width="50">
            <template slot-scope="t,r,i">
              <span> {{i+1}} </span>
            </template>
          </a-table-column>
          <a-table-column title="标题" dataIndex="title" :width="150" align="center">
            <template slot-scope="t,r,i">
              <span> {{t}} </span>
            </template>
          </a-table-column>
          <a-table-column title="任务名称" dataIndex="name" :width="150" align="center">
            <template slot-scope="t">
              <span> {{t}} </span>
            </template>
          </a-table-column>
          <a-table-column title="所属流程" dataIndex="processName" :width="150" align="center">
            <template slot-scope="t">
              <span> {{t}} </span>
            </template>
          </a-table-column>
          <a-table-column title="委托人" dataIndex="owner" align="center" :width="150">
            <template slot-scope="t">
              <span> {{t}} </span>
            </template>
          </a-table-column>
          <a-table-column title="流程发起人" dataIndex="applyer" :width="150" align="center">
            <template slot-scope="t">
              <span> {{t}} </span>
            </template>
          </a-table-column>
          <a-table-column title="优先级" dataIndex="priority" :width="110" align="center"
                          key="so" :sorter="(a,b)=>a.priority - b.priority"
          >
            <template slot-scope="t">
              <span v-if="t==0" style="color: green;"> 普通 </span>
              <span v-else-if="t==1" style="color: orange;"> 重要 </span>
              <span v-else-if="t==2" style="color: red;"> 紧急 </span>
              <span v-else style="color: #999;"> 无 </span>
            </template>
          </a-table-column>
          <a-table-column title="状态" dataIndex="isSuspended" :width="100" align="center"
                          key="z" :sorter="(a,b)=>Boolean(a.isSuspended)?0:1 - Boolean(b.isSuspended)?0:1"
          >
            <template slot-scope="t">
              <span v-if="!Boolean(t)" style="color: green;"> 已激活 </span>
              <span v-if="Boolean(t)" style="color: orange;"> 已挂起 </span>
            </template>
          </a-table-column>
          <a-table-column title="创建时间" dataIndex="createTime" :width="200" align="center">
            <template slot-scope="t">
              <span>{{t}}</span>
            </template>
          </a-table-column>
          <a-table-column title="操作" dataIndex="" align="center">
            <template slot-scope="t,r,i">
              <a href="javascript:void(0);" @click="detail(r)" style="color: blue">查看并处理</a>
              <a-divider type="vertical"/>
              <span v-if="Boolean(r.isSuspended)" style="cursor: no-drop;color: #999999;" title="流程已被挂起，无法操作！">
                查看并处理 <a-divider type="vertical"/>
                委托他人代办 <a-divider type="vertical"/>
              </span>
              <span v-else>
                <!--<a href="javascript:void(0);" @click="passTask(r)" style="color: green">通过</a>
                <a-divider type="vertical" />
                <a href="javascript:void(0);" @click="backTask(r)" style="color: orange">驳回</a>
                <a-divider type="vertical" />-->
                <a href="javascript:void(0);" @click="delegateTask(r)" style="color: #00A0E9">委托他人代办</a>
                <a-divider type="vertical"/>
              </span>
              <a href="javascript:void(0);" @click="history(r)" style="color: #217dbb">历史</a>
            </template>
          </a-table-column>
        </a-table>
      </a-row>
    </a-card>
    <!---->
    <a-modal title="审批历史" v-model="modalLsVisible" :mask-closable="false" :width="'80%'" :footer="null">
      <div v-if="modalLsVisible">
        <component :is="historicDetail" :procInstId="procInstId"></component>
      </div>
    </a-modal>
    <sign-modal :form="form" :modal-task-title="modalTaskTitle" :modal-task-visible="modalTaskVisible"
                :assignee-list="assigneeList" :user-loading="userLoading" :back-list="backList"
                @cancel="modalTaskVisible = false" @afterSub="afterSub"
                :show-assign="showAssign" :back-loading="backLoading"></sign-modal>
  </div>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { activitiMixin } from '@/views/activiti/mixins/activitiMixin'
  import { setStore } from '@/utils/storage'
  import SignModal from './signModal'
  import activitiSetting from './mixins/activitiSetting'
  import { getAction, postFormAction } from '@/api/manage'

  export default {
    name: 'todo-manage',
    mixins: [activitiMixin, JeecgListMixin],
    components: { SignModal },
    data() {
      return {
        openSearch: true,
        openTip: true,
        loading: true, // 表单加载状态
        modalTaskVisible: false,
        userLoading: false,
        backLoading: false,
        selectCount: 0, // 多选计数
        selectList: [], // 多选数据
        assigneeList: [],
        error: '',
        showAssign: true,
        searchForm: {
          // 搜索框对应data对象
          name: ''
        },
        backList: [
          {
            key: '-1',
            name: '发起人'
          }
        ],
        modalTaskTitle: '',
        modalTitle: '', // 添加或编辑标题
        form: {
          id: '',
          userId: '',
          procInstId: '',
          comment: '',
          type: 0,
          assignees: [],
          backTaskKey: '-1',
          sendMessage: true,
          sendSms: false,
          sendEmail: false
        },
        submitLoading: false, // 添加或编辑提交状态
        data: [], // 表单数据
        total: 0, // 表单数据总数
        dictPriority: [],
        isGateway: false,
        lcModa: {
          title: '',
          disabled: false,
          visible: false,
          formComponent: null,
          isNew: false
        },
        url: {
          todoList: '/actTask/todoList',
          passAll: '/actTask/passAll/',
          backAll: '/actTask/backAll/'
        },
        /*历史*/
        modalLsVisible: false,
        procInstId: ''
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      afterSub(formData) {
        this.getDataList()
        this.modalTaskVisible = false
      },
      init() {
        this.getDataList()
      },
      loadData() {
      },
      getDataList() {
        this.loading = true
        getAction(this.url.todoList, this.searchForm).then(res => {
          this.loading = false
          if (res.success) {
            this.data = res.result || []
            this.total = this.data.leading
          }
        })
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field
          this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        this.ipagination = pagination
        // this.loadData();
      },
      handleSearch() {
        this.getDataList()
      },
      handleReset() {
        this.searchForm = {}
        // 重新加载数据
        this.getDataList()
      },
      clearSelectAll() {
        this.$refs.table.selectAll(false)
      },
      detail(r) {
        if (!r.routeName) {
          this.$message.warning(
            '该流程信息未配置表单，请联系开发人员！'
          )
          return
        }
        this.lcModa.disabled = true
        this.lcModa.title = r.title
        this.lcModa.processData = r
        this.lcModa.isNew = false
        this.lcModa.from = activitiSetting.todoManage
        this.lcModa.isTask = true
        setStore('lcModa', this.lcModa)
        this.$router.push(activitiSetting.applyFormPath)
      },
      delegateTask(v) {
        this.modalTaskTitle = '委托他人代办'
        this.form.id = v.id
        this.form.procInstId = v.procInstId
        this.form.type = 2
        this.showAssign = false
        this.modalTaskVisible = true
      },
      history(v) {
        if (!v.procInstId) {
          this.$message.error('流程实例ID不存在')
          return
        }
        this.procInstId = v.procInstId
        this.modalLsVisible = true
      },
      passAll() {
        if (this.selectCount <= 0) {
          this.$message.warning('您还未选择要通过的数据')
          return
        }
        // 批量通过
        this.$confirm({
          title: '确认通过',
          content:
            '您确认要通过所选的 ' +
            this.selectCount +
            ' 条数据? 注意：将默认分配给节点设定的所有可审批用户',
          loading: true,
          onOk: () => {
            let ids = ''
            this.selectList.forEach(function(e) {
              ids += e.id + ','
            })
            ids = ids.substring(0, ids.length - 1)
            postFormAction(this.url.passAll, { ids: ids }).then(res => {
              if (res.success) {
                this.$message.success('操作成功')
                this.clearSelectAll()
                this.getDataList()
              }
            })
          }
        })
      },
      backAll() {
        if (this.selectCount <= 0) {
          this.$message.warning('您还未选择要驳回的数据')
          return
        }
        // 批量驳回
        this.$confirm({
          title: '确认驳回',
          content:
            '您确认要驳回所选的 ' +
            this.selectCount +
            ' 条数据? 注意：所有流程将驳回至发起人',
          loading: true,
          onOk: () => {
            let procInstIds = ''
            this.selectList.forEach(function(e) {
              procInstIds += e.procInstId + ','
            })
            procInstIds = procInstIds.substring(0, procInstIds.length - 1)
            postFormAction(this.url.backAll, { procInstIds: procInstIds }).then(res => {
              if (res.success) {
                this.$message.success('操作成功')
                this.clearSelectAll()
                this.getDataList()
              }
            })
          }
        })
      }
    }

  }
</script>