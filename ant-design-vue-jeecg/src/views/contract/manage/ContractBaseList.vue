<template>
  <a-card :bordered="false">
    <a-tabs default-active-key="1" @change="handleTabsChange">
      <a-tab-pane v-if="showPanel('T0')" key="T0" tab="采购类">
        <contract-purchase-list></contract-purchase-list>
      </a-tab-pane>
      <a-tab-pane v-if="showPanel('T1')" key="T1" tab="销售类" force-render>
        Content of Tab Pane 2
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>

  import ContractPurchaseList from './ContractPurchaseList'
  import { getAction } from '@/api/manage'
  export default {
    name: 'ContractBaseList',
    // mixins: [JeecgListMixin],
    components: { ContractPurchaseList },
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
    computed: {

    },
    methods: {
      showPanel(code){
        return this.contractTypeList.map(m=>m.code).includes(code);
      },
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