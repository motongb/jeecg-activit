<template>
  <div>
    <a-card>
      <a-table
        :columns="columns"
        :dataSource="data"
        :pagination="false">
        <template v-for="(col, i) in columnField" :slot="col" slot-scope="text, record, index">
          <template v-if="col==='payDate'">
            <a-date-picker v-if="record.editable" v-model="record[col]"/>
            <template v-else>{{ text }}</template>
          </template>
          <template v-else>
            <a-input
              :key="col"
              v-if="record.editable"
              v-model="record[col]"
              :placeholder="columns[i].title"/>
            <template v-else>{{ text }}</template>
          </template>
        </template>
        <template v-if="!disabled" slot="operation" slot-scope="text, record, index">
          <template v-if="record.editable">
              <span v-if="record.isNew">
                <a @click="saveRow(index)">添加</a>
                <a-divider type="vertical"/>
                <a-popconfirm title="是否要删除此行？" @confirm="remove(index)">
                  <a>删除</a>
                </a-popconfirm>
              </span>
            <span v-else>
                <a @click="saveRow(index)">保存</a>
                <a-divider type="vertical"/>
                <a @click="cancel(index)">取消</a>
              </span>
          </template>
          <span v-else>
              <a @click="toggle(index)">编辑</a>
              <a-divider type="vertical"/>
              <a-popconfirm title="是否要删除此行？" @confirm="remove(index)">
                <a>删除</a>
              </a-popconfirm>
            </span>
        </template>
      </a-table>
      <a-button v-if="!disabled" style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="plus"
                @click="newMember">
        新增
      </a-button>
    </a-card>
  </div>
</template>

<script>
  import moment from 'moment'
  import { getAction } from '@/api/manage'

  export default {
    name: 'ContractPayment',
    props: {
      contractId: {
        type: String,
        default: null
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data() {
      return {
        columnField: ['rowIndex', 'context', 'payDate', 'payDept', 'payReceive', 'payCondition', 'payAmount', 'duty'],
        // table
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '款项内容',
            align: 'center',
            dataIndex: 'context',
            scopedSlots: { customRender: 'context' }
          },
          {
            title: '付款日期',
            align: 'center',
            dataIndex: 'payDate',
            scopedSlots: { customRender: 'payDate' }
          },
          {
            title: '付款单位',
            align: 'center',
            dataIndex: 'payDept',
            scopedSlots: { customRender: 'payDept' }
          },
          {
            title: '收款方',
            align: 'center',
            dataIndex: 'payReceive',
            scopedSlots: { customRender: 'payReceive' }
          },
          {
            title: '付款条件',
            align: 'center',
            dataIndex: 'payCondition',
            scopedSlots: { customRender: 'payCondition' }
          },
          {
            title: '付款金额',
            align: 'center',
            dataIndex: 'payAmount',
            scopedSlots: { customRender: 'payAmount' }
          },
          {
            title: '违约责任',
            align: 'center',
            dataIndex: 'duty',
            scopedSlots: { customRender: 'duty' }
          },
          {
            title: '操作',
            key: 'action',
            width: 120,
            scopedSlots: { customRender: 'operation' }
          }
        ],
        data: [],
        rowNum: 0,
        payDate: moment().subtract(-1, 'years').format('YYYY-MM-DD'),
        url: {
          list: '/contract/contractPayment/list'
        }
      }
    },
    watch: {
      contractId() {
        this.initValue()
      }
    },
    created() {
      if (this.contractId) {
        this.initValue()
      }
    },
    methods: {
      initValue() {
        getAction(this.url.list, { contractId: this.contractId }).then(res => {
          if (res.success) {
            res.result.records.forEach(item => {
              item.editable = false
              item.isNew = true
              item.key = ++this.rowNum
            })
            this.data = res.result.records
          }
        })
      },
      getData() {
        return this.data
      },
      newMember() {
        this.data.push({
          key: ++this.rowNum,
          payDate: this.payDate,
          editable: true,
          isNew: true
        })
      },
      remove(index) {
        this.data.splice(index, 1)
      },
      toggle(index) {
        this.data[index].editable = !this.data[index].editable
      },
      cancel(index) {
        this.data[index].editable = false
      },
      saveRow(index) {
        let target = this.data[index]
        target.editable = false
        target.isNew = false
      }
    }
  }
</script>

<style scoped>

</style>