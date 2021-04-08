<template>
  <a-table
    rowKey="id"
    size="middle"
    bordered
    :loading="loading"
    :columns="columns"
    :dataSource="dataSource"
    :pagination="false">

  </a-table>
</template>

<script>
  import { getAction } from '@api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { filterDictTextByDictCode } from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'SysRuleItemSubTable',
    mixins: [JeecgListMixin],
    props: {
      record: {
        type: Object,
        default: null
      }
    },
    data() {
      return {
        description: '自定义规则项内嵌列表',
        disableMixinCreated: true,
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '规则项类型',
            align: 'center',
            dataIndex: 'itemType',
            customRender: (t, r, index) => filterDictTextByDictCode('rule_item_type', t)
          },
          {
            title: '项值',
            align: 'center',
            dataIndex: 'itemVal',
            customRender: (t, r, index) => {
              switch (r.itemType) {
                case '2':
                  return t + '级'
                case '4':
                  return t + '位数'
                case '5':
                  return filterDictTextByDictCode('flow_number_reset', t)
                default:
                  return t
              }
            }
          },
          {
            title: '排序',
            align: 'center',
            dataIndex: 'sort'
          }
        ],
        url: {
          listByMainId: '/contract/sysRule/querySysRuleItemByMainId'
        }
      }
    },
    watch: {
      record: {
        immediate: true,
        handler() {
          if (this.record != null) {
            this.loadData(this.record)
          }
        }
      }
    },
    methods: {

      loadData(record) {
        this.loading = true
        this.dataSource = []
        getAction(this.url.listByMainId, {
          id: record.id
        }).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
          }
        }).finally(() => {
          this.loading = false
        })
      }

    }
  }
</script>

<style scoped>

</style>
