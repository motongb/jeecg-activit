<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    fullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <!--    <contract-purchase-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></contract-purchase-form>-->
    <component :disabled="lcModa.disabled" :is="lcModa.formComponent" ref="processForm"
               :processData="lcModa.processData" :isNew="lcModa.isNew" :title="lcModa.title"
               :task="lcModa.isTask" :dept="lcModa.dept" :tableId="lcModa.processData.tableId"></component>

  </j-modal>
</template>

<script>

  import ContractPurchaseForm from './ContractPurchaseForm'
  import { getAction } from '@/api/manage'
  import { activitiMixin } from '@/views/activiti/mixins/activitiMixin'

  export default {
    name: 'ContractPurchaseModal',
    components: {
      ContractPurchaseForm
    },
    mixins: [activitiMixin],
    data() {
      return {
        title: '',
        width: 800,
        visible: false,
        disableSubmit: false,
        url: {
          queryNewestProcess: '/activiti_process/queryNewestProcess'
        },
        lcModa: { processData: { tableId: undefined } }
      }
    },
    methods: {
      add() {
        this.visible = true
        this.$nextTick(() => {
          this.$refs.realForm.add()
        })
      },
      edit(record) {
        getAction(this.url.queryNewestProcess, { processKey: record.processDef }).then(res => {
          if (res.success) {
            this.visible = true
            let processData = res.result[0]
            this.lcModa.formComponent = this.getFormComponent(processData.routeName).component
            this.lcModa.disabled = false
            this.lcModa.title = processData.title
            this.lcModa.processData = processData
            this.lcModa.processData.tableId = record.id
            this.lcModa.isNew = false
          }
        })
        // this.$nextTick(()=>{
        //   this.$refs.realForm.edit(record);
        // })
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        // this.$refs.realForm.submitForm()
        if (this.disableSubmit){
          return
        }
        this.disableSubmit = true
        this.$refs.processForm.handleSubmit().then(res => {
          this.submitCallback()
          this.disableSubmit = false
        }).catch(_ => {
          this.disableSubmit = false
        })
      },
      submitCallback() {
        this.$emit('ok')
        this.visible = false
      },
      handleCancel() {
        this.close()
      }
    }
  }
</script>