<template>
  <div>
    <a-modal title="用印申请" :visible="modalTaskVisible" @cancel="cancel" :width="600">
      <a-radio-group v-if="processList.length>0" v-model="selectedIndex">
        <template v-for="(item,index) in processList">
          <a-radio-button class="radioStyle" :value="index">{{item.name}}</a-radio-button>
        </template>
      </a-radio-group>
      <a-empty v-else description="无可用流程"/>
      <div slot="footer">
        <a-button type="text" @click="cancel">取消</a-button>
        <a-button type="primary" :loading="submitLoading" @click="handelSubmit">提交</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
  import { getAction } from '@/api/manage'
  import moment from 'moment'
  import activitiSetting from '../../activiti/mixins/activitiSetting'
  import { setStore } from '@/utils/storage'

  export default {
    name: 'ApplyYinz',
    props: {
      typeCode: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        radioStyle: {
          display: 'block',
          height: '30px',
          lineHeight: '30px'
        },
        modalTaskVisible: false,
        submitLoading: false,
        processList: [],
        queryParam: {
          status: 1,
          roles: true,
          zx: true,
          typeId: this.typeCode
        },
        url: {
          getProcessDataList: '/activiti_process/listData'
        },
        selectedIndex: -1,
        record: {}
      }
    },
    created() {
      this.initData()
    },
    methods: {
      initData() {
        getAction(this.url.getProcessDataList, this.queryParam).then(res => {
          this.processList = res.result
          console.log(this.processList)
        })
      },
      show(record) {
        this.modalTaskVisible = true
        this.record = record
      },
      handelSubmit() {
        if (this.selectedIndex < 0) {
          this.$message.warning('请选择流程')
          return
        }
        let r = this.processList[this.selectedIndex]
        console.log('用印', r)
        let lcModa = {}
        lcModa.title = moment().format('YYYY-MM-DD HH:mm:ss') + r.name
        lcModa.isNew = true
        lcModa.processData = r
        lcModa.from = activitiSetting.contractPurchaseList
        lcModa.record = this.record
        setStore('lcModa', lcModa)
        this.$router.push(activitiSetting.applyFormPath)
      },
      cancel() {
        this.modalTaskVisible = false
      }
    }
  }
</script>

<style scoped>
  .radioStyle {
    display: block;
    margin-top: 10px;
  }
</style>