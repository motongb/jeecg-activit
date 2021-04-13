<template>
  <a-card :bordered="false">
    <a-tabs default-active-key="T01" @change="handleTabsChange">
      <a-tab-pane key="T01" tab="采购类">
        <lg-contract-purchase-list></lg-contract-purchase-list>
      </a-tab-pane>
      <a-tab-pane key="T02" tab="销售类" force-render>
        Content of Tab Pane 2
      </a-tab-pane>
      <a-tab-pane key="T03" tab="工程类" force-render>
        Content of Tab Pane 2
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>

  import { getAction } from '@/api/manage'
  import LgContractPurchaseList from './LgContractPurchaseList'

  export default {
    name: 'ContractBaseList',
    components: { LgContractPurchaseList },
    data() {
      return {
        url: {
          treeList: '/contract/contractType/tree'
        },
        contractTypeList: []
      }
    },
    created() {
      this.getContractType()
    },
    computed: {},
    methods: {
      getContractType() {
        getAction(this.url.treeList, { roles: true }).then(res => {
          if (res.success) {
            this.contractTypeList = res.result
          }
        })
      },
      handleTabsChange(key) {
        console.log(key)
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>