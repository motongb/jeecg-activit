<template>
  <div>
    <wps-view-tag ref="wpsView" :showSaveBtn="true" :file-id="fileId" @save="handleSave"
                  @fileOpen="fileOpenCallback"></wps-view-tag>
  </div>
</template>

<script>
  import WpsViewTag from './WpsViewTag'
  import { putAction } from '@/api/manage'

  export default {
    name: 'ModelEditPage',
    components: { WpsViewTag },
    data() {
      return {
        fileId: '',
        modelId: '',
        url: {
          edit: '/contract/contractModel/edit'
        }
      }
    },
    mounted() {
      let params = this.$route.query
      console.log(params)
      this.fileId = params.fileId
      this.modelId = params.id
      this.$refs.wpsView.init()
    },
    methods: {
      fileOpenCallback(file) {
        if (file) {
          this.fileId = file.id.split('-')[1]
        }
        console.log(this.fileId)
      },
      handleSave() {
        putAction(this.url.edit, { id: this.modelId, fileId: this.fileId }).then(res => {
          if (res.success) {
            this.closed()
          }
        })
      },
      closed() {
        this.$bus.$emit('closed-current-tabs')
      }
    }
  }
</script>

<style scoped>

</style>