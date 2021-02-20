<template>
  <div>
    <a-card>
      <a-table
        :columns="columns"
        :dataSource="data"
        :pagination="false">
        <template slot="bankCode" slot-scope="text, record">
          <a-input v-if="record.editable" v-model="record.bankCode" placeholder="银行代码"/>
          <template v-else>{{ text }}</template>
        </template>
        <template slot="bank" slot-scope="text, record">
          <a-input v-if="record.editable" v-model="record.bank" placeholder="开户行"/>
          <template v-else>{{ text }}</template>
        </template>
        <template slot="bankName" slot-scope="text, record">
          <a-input v-if="record.editable" v-model="record.bankName" placeholder="账户名称"/>
          <template v-else>{{ text }}</template>
        </template>
        <template slot="bankNo" slot-scope="text, record">
          <a-input v-if="record.editable" v-model="record.bankNo" placeholder="账号"/>
          <template v-else>{{ text }}</template>
        </template>
        <!--        <template slot="type" slot-scope="text, record">-->
        <!--          <a-input v-if="record.editable" v-model="record.type" placeholder="类型"/>-->
        <!--          <template v-else>{{ text }}</template>-->
        <!--        </template>-->
        <template slot="settleWay" slot-scope="text, record">
          <j-dict-select-tag v-if="record.editable" v-model="record.settleWay" dictCode="company_settle_way"/>
          <template v-else>{{filterDictTextByDictCode('company_settle_way', text)}}</template>
        </template>
        <template slot="operation" slot-scope="text, record, index">
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
      <a-button style="width: 100%; margin-top: 16px; margin-bottom: 8px" type="dashed" icon="plus" @click="newMember">
        新增
      </a-button>
    </a-card>
  </div>
</template>

<script>
  import { getAction } from '@/api/manage'
  import {filterDictTextByDictCode} from '@comp/dict/JDictSelectUtil'
  export default {
    name: 'CompanyBankItem',
    props: {
      companyId: {
        type: String,
        default: null
      }
    },
    data() {
      return {
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
            title: '银行代码',
            align: 'center',
            dataIndex: 'bankCode',
            scopedSlots: { customRender: 'bankCode' }
          },
          {
            title: '开户行',
            align: 'center',
            dataIndex: 'bank',
            scopedSlots: { customRender: 'bank' }
          },
          {
            title: '账户名称',
            align: 'center',
            dataIndex: 'bankName',
            scopedSlots: { customRender: 'bankName' }
          },
          {
            title: '账号',
            align: 'center',
            dataIndex: 'bankNo',
            scopedSlots: { customRender: 'bankNo' }
          },
          // {
          //   title: '类型',
          //   align: 'center',
          //   dataIndex: 'type',
          //   scopedSlots: { customRender: 'type' }
          // },
          {
            title: '结算方式',
            align: 'center',
            dataIndex: 'settleWay',
            scopedSlots: { customRender: 'settleWay' }
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
          list: '/contract/companyBank/list'
        }
      }
    },
    watch: {
      companyId() {
        if (this.companyId) {
          this.initValue()
        } else {
          this.data = []
        }
      }
    },
    created() {
      if (this.companyId) {
        this.initValue()
      }
    },
    methods: {
      filterDictTextByDictCode(dictCode,key){
        return filterDictTextByDictCode(dictCode,key)
      },
      initValue() {
        getAction(this.url.list, { companyId: this.companyId }).then(res => {
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
          settleWay: '1',
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