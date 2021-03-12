<template>
  <div>
    <a-auto-complete :disabled="disabled"  v-model="text" :data-source="dataSource" optionLabelProp="label" style="width: 100%"
                     @select="onSelect" @search="onSearch">
      <template slot="dataSource">
        <a-select-option v-for="(item,index) in dataSource" :key="index" :value="item.fileId" :label="item.name">
          {{ item.name }}
        </a-select-option>
      </template>
      <a-input>
        <a-icon slot="suffix" type="search" @click="showModal"/>
      </a-input>
    </a-auto-complete>

    <j-modal
      :visible="visible"
      title="模板选择"
      switchFullscreen
      wrapClassName="j-user-select-modal"
      @ok="handleSubmit"
      @cancel="close"
      style="top:50px"
      cancelText="关闭">
      <a-card>
        用户账号:
        <a-input-search
          :style="{width:'150px',marginBottom:'15px'}"
          placeholder="请输入账号"
          v-model="queryParam.name"
          @search="onSearch"
        ></a-input-search>
        <a-button @click="searchReset(1)" style="margin-left: 20px" icon="redo">重置</a-button>
        <a-table
          ref="table"
          size="middle"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange,type: 'radio'}"
          :loading="loading"
          @change="handleTableChange">
        </a-table>
      </a-card>
    </j-modal>
  </div>
</template>

<script>
  import { getAction } from '@/api/manage'

  export default {
    name: 'ModelSelect',
    model: {
      prop: 'value',
      event: 'change'
    },
    props: {
      disabled:{
        type:Boolean,
        default:false
      },
      value: {
        type: String,
        default: undefined
      }
    },
    data() {
      return {
        text: '',
        columns: [
          {
            title: '模板名称',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '编码',
            align: 'center',
            dataIndex: 'modelKey'
          }
        ],
        visible: false,
        dataSource: [],
        queryParam: { pageNo: 1, pageSize: 10, name: '' },
        ipagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        selectedRowKeys: [],
        isorter: {
          column: 'createTime',
          order: 'desc'
        },
        loading: false,
        url: {
          list: '/contract/contractModel/list',
        }
      }
    },
    watch: {
      value() {
        if (this.value) {
          this.queryById()
        }
      }
    },
    created() {
      this.loadData()
    },
    methods: {
      queryById() {
        getAction(this.url.list, { fileId: this.value }).then(res => {
          if (res.success) {
            this.text = res.result.records[0].name
          }
        })
      },
      onSelect(value) {
        console.log('onSelect', value)
        this.$emit('change', value)
      },
      searchReset(pageNum) {
        this.queryParam.pageNo = pageNum
        this.queryParam.name = ''
        this.loadData()
      },
      onSearch(searchText) {
        this.queryParam.name = searchText
        this.loadData()
      },
      close() {
        this.visible = false
      },
      showModal() {
        this.visible = true
      },
      handleSubmit() {
        this.$emit('change', this.selectedRowKeys[0])
        this.close()
      },
      loadData() {
        if (this.loading) {
          return
        }
        this.loading = true
        getAction(this.url.list, this.queryParam).then(res => {
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
          this.loading = false
        })
      },
      handleTableChange(pagination, filters, sorter) {
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field
          this.isorter.order = 'ascend' === sorter.order ? 'asc' : 'desc'
        }
        this.ipagination = pagination
        this.queryParam.pageNo = pagination.current
        this.queryParam.pageSize = pagination.pageSize
        this.loadData()
      },
      onSelectChange(selectedRowKeys) {
        this.selectedRowKeys = selectedRowKeys
        console.log(selectedRowKeys)
      }
    }
  }
</script>

<style scoped>

</style>