<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <lg-contract-purchase-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></lg-contract-purchase-form>
  </j-modal>
</template>

<script>

  import LgContractPurchaseForm from './LgContractPurchaseForm'
  export default {
    name: 'LgContractPurchaseModal',
    components: {
      LgContractPurchaseForm
    },
    data () {
      return {
        title:'',
        width:1024,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      add () {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add();
        })
      },
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.submitForm();
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>