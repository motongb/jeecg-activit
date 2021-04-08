<template>
  <div>
    <j-modal
      :visible="visible"
      title="公司选择"
      :width="1000"
      switchFullscreen
      wrapClassName="j-user-select-modal"
      @ok="handleSubmit"
      @cancel="close"
      style="top:50px"
      cancelText="关闭">
      <a-card>
        公司名称:
        <a-input-search :style="{width:'150px',marginBottom:'15px'}" v-model="queryParam.name"
                        @search="onSearch"></a-input-search>
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
    name: 'CompanySelect',
    data() {
      return {
        queryParam: {},
        columns: [
          {
            title: '中文名称',
            align: 'center',
            dataIndex: 'nameCn'
          },
          {
            title: '英文名称',
            align: 'center',
            dataIndex: 'nameEn'
          },
          {
            title: '编码',
            align: 'center',
            dataIndex: 'code'
          },
          {
            title: '法人',
            align: 'center',
            dataIndex: 'legal'
          },
          {
            title: '信用代码',
            align: 'center',
            dataIndex: 'creditCode'
          }
        ],
        dataSource: [],
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
        selectedRows: [],
        visible: false,
        loading: false,
        url: {
          list: '/contract/company/list'
        }
      }
    },
    created() {
      this.initData()
    },
    methods: {
      initData() {
        getAction(this.url.list, {}).then(res => {
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
        })
      },
      show() {
        console.log(this.visible)
        this.visible = true
      },
      handleTableChange() {
      },
      onSelectChange(selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys
        this.selectedRows = selectedRows
      },
      searchReset(pageNum) {
      },
      onSearch() {
      },
      close() {
        this.visible = false
      },
      handleSubmit() {
        this.$emit('selected', { id: this.selectedRows[0].id, name: this.selectedRows[0].nameCn })
        this.close()
      }
    }
  }
</script>

<style scoped>

</style>