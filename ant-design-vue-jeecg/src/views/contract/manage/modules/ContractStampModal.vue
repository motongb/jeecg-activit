<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    fullscreen
    @cancel="close"
    :okButtonProps="{ class:{'jee-hidden': true} }"
    cancelText="关闭">
    <contract-stamp-form ref="realForm" :lc-moda="lcModa"></contract-stamp-form>
  </j-modal>
</template>

<script>

  import ContractStampForm from './ContractStampForm'

  export default {
    name: 'ContractStampModal',
    components: {
      ContractStampForm
    },
    data() {
      return {
        title: '',
        width: 950,
        visible: false,
        lcModa: {
          disabled: false,
          isNew: false,
          processData: {}
        }
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
        this.visible = true
        this.lcModa.disabled = true
        this.lcModa.processData.tableId = record.id
        this.lcModa.isNew = false
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
    }
  }
</script>