<template>
  <div>
    <a-collapse v-model="activeKey">
      <a-collapse-panel v-for="(item,index) in dataList" :key="item.id" :header="item.name">
        <a-list :grid="{ gutter: 10,column:4}" :dataSource="item.children">
          <a-list-item slot="renderItem" slot-scope="child">
            <a-card>
              <div slot="title">
                <a-row>
                  <a-col span="12" :title="child.name">{{child.name}}</a-col>
                  <a-col span="12" style="text-align: right;">
                    <a href="javascript:void (0)" @click="chooseProcess(child)">发起申请</a>
                  </a-col>
                </a-row>
              </div>
              {{item.description}}
            </a-card>
          </a-list-item>
        </a-list>
      </a-collapse-panel>
    </a-collapse>
  </div>
</template>

<script>
  import { setStore } from '@/utils/storage'
  import activitiSetting from '../../activiti/mixins/activitiSetting'
  import { formatDate } from '@/utils/util'

  export default {
    name: 'DraftPage',
    data() {
      return {
        activeKey: [],
        dataList: [],
        url: {
          treeList: '/contract/contractType/tree',
          queryNewestProcess: '/activiti_process/queryNewestProcess'
        }
      }
    },
    created() {
      this.loadData()
    },
    methods: {
      loadData() {
        this.getAction(this.url.treeList, { roles: true }).then(res => {
          if (res.success) {
            this.dataList = res.result
            this.dataList.forEach(item => this.activeKey.push(item.id))
          }
        })
      },
      chooseProcess(child) {
        this.getAction(this.url.queryNewestProcess, { processKey: child.processDef }).then(res => {
          if (res.success && res.result.length > 0) {
            let lcModa = {}
            lcModa.from = activitiSetting.draftPagePath
            lcModa.processData = res.result[0]
            lcModa.isNew = true
            lcModa.title = formatDate(new Date().getTime(), 'yyyy-MM-dd').replaceAll('-', '') + child.name
            lcModa.typeCode = child.code
            setStore('lcModa', lcModa)
            this.$router.push(activitiSetting.applyFormPath)
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>