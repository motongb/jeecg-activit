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
    <test-sign-form :processData="processData" ref="realForm" @ok="submitCallback"
                    :disabled="disableSubmit"></test-sign-form>
  </j-modal>
</template>

<script>

  import TestSignForm from './TestSignForm'

  export default {
    name: 'TestSignModal',
    components: {
      TestSignForm
    },
    data() {
      return {
        processData: {},
        title: '',
        width: 800,
        visible: false,
        disableSubmit: false
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
        this.processData = { tableId: record.id, tableName: 'test_sign' }
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
        this.$refs.realForm.submitForm()
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