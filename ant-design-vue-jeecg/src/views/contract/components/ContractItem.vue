<template>
  <div>
    <a-card>
      <a-table
        :columns="columns"
        :dataSource="data"
        :pagination="false">
        <template v-for="(col, i) in columnField" :slot="col" slot-scope="text, record, index">
          <template v-if="col==='unit'">
            <j-dict-select-tag :disabled="!record.editable" v-model="record[col]" dictCode="contract_item_unit"/>
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
      <a-button v-if="!disabled" style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="plus" @click="newMember">
        新增
      </a-button>
    </a-card>
  </div>
</template>

<script>
  export default {
    name: 'ContractItem',
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
        columnField: ['rowNo', 'name', 'purchaseWay', 'model', 'unit', 'price', 'rate', 'number', 'total'],
        // table
        columns: [
          {
            title: '序号',
            align: 'center',
            dataIndex: 'rowNo',
            key: 'rowNo',
            width: 60,
            customRender: (t, r, index) => {
              return parseInt(index) + 1
            }
          },
          {
            title: '产品服务名称',
            align: 'center',
            dataIndex: 'name',
            key: 'name',
            scopedSlots: { customRender: 'name' }
          },
          {
            title: '采购方式',
            align: 'center',
            dataIndex: 'purchaseWay',
            key: 'purchaseWay',
            scopedSlots: { customRender: 'purchaseWay' }
          },
          {
            title: '型号规格',
            align: 'center',
            dataIndex: 'model',
            key: 'model',
            scopedSlots: { customRender: 'model' }
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'unit',
            key: 'unit',
            width: 100,
            scopedSlots: { customRender: 'unit' }
          },
          {
            title: '含税单价',
            align: 'center',
            dataIndex: 'price',
            key: 'price',
            scopedSlots: { customRender: 'price' }
          },
          {
            title: '税率',
            align: 'center',
            dataIndex: 'rate',
            key: 'rate',
            scopedSlots: { customRender: 'rate' }
          },
          {
            title: '数量',
            align: 'center',
            dataIndex: 'number',
            key: 'number',
            scopedSlots: { customRender: 'number' }
          },
          {
            title: '小计',
            align: 'center',
            dataIndex: 'total',
            key: 'total',
            scopedSlots: { customRender: 'total' }
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
        url: {
          list: '/contract/contractItem/list'
        }
      }
    },
    created() {
      if (this.contractId) {
        this.initValue()
      }
    },
    methods: {
      initValue() {
        this.getAction(this.url.list, { contractId: this.contractId }).then(res => {
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
        this.data.push({ key: ++this.rowNum, unit: '0', editable: true, isNew: true })
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