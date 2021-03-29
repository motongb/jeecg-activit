<template>
  <a-spin :spinning="loading">
    <div>
      <a-upload
        name="file"
        :action="uploadAction"
        :showUploadList="false"
        :data="{'biz':bizPath}"
        :headers="headers"
        @change="handleChange">
        <a v-show="showOpenDocBtn" href="javascript:void(0);" class="open-doc">打开本地文件</a>
      </a-upload>
      <a v-show="showSaveBtn" href="javascript:void(0);" class="open-doc" @click="closed">保存并关闭</a>
      <div id="viewFile"></div>
    </div>
  </a-spin>
</template>

<script>
  import WPS from '@/utils/wps'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from '@/store/mutation-types'
  import { saveWpsModel, createNewFile, getPreViewUrl } from '@/api/wpsApi'


  export default {
    name: 'WpsViewTag',
    props: {
      isModel: {
        type: Boolean,
        default: false
      },
      showOpenDocBtn: {
        type: Boolean,
        default: true
      },
      showSaveBtn: {
        type: Boolean,
        default: false
      },
      docType: {
        type: String,
        default: 'word'
      },
      simpleMode: {
        type: Boolean,
        default: false
      },
      permission: {
        type: String,
        default: 'write'
      }
    },
    data() {
      return {
        // 加载状态
        loading: false,
        // 文件上传文件夹
        bizPath: 'temp',
        token: Vue.ls.get(ACCESS_TOKEN),
        headers: {},
        uploadAction: window._CONFIG['domianURL'] + '/sys/common/upload',
        model: {},
        wps: null,
        docUrl: ''
      }
    },
    mounted() {
      this.headers = { 'X-Access-Token': this.token }
    },
    methods: {
      /*打开本地文件回调*/
      handleChange({ file, fileList }) {
        this.loading = true
        if (file.status === 'uploading') {

        }
        if (file.status === 'done') {
          let oaModel = {
            name: file.name,
            size: file.size,
            downloadUrl: file.response.message,
            fileType: this.docType
          }
          saveWpsModel(oaModel).then(res => {
            if (res.success) {
              this.model = res.result.model
              this.openDoc(res.result.preViewUrl)
              this.urlChange(res.result.preViewUrl)
            }
          })
        }
      },
      openDoc(url) {
        this.loading = true
        this.wps = WPS.config({
          mode: this.simpleMode ? 'simple' : 'normal',
          mount: document.querySelector('#viewFile'),
          wpsUrl: url,
          onHyperLinkOpen: ({ linkUrl }) => {  // 拦截外链跳转函数
            console.log(linkUrl)
            this.openDoc(linkUrl)
            this.urlChange(linkUrl)
          }
        })
        this.wps.setToken({ token: this.token })
        this.wps.on('fileOpen', data => {
          if (this.isModel) {
            return
          }
          this.$emit('fileOpen', data.fileInfo)
        })
        const iframe = this.wps.iframe
        if (iframe.attachEvent) {
          iframe.attachEvent('onload', () => this.loaded())
        } else {
          iframe.onload = () => this.loaded()
        }
      },
      async init(fileId) {
        console.log(fileId)
        if (fileId && fileId.length > 0) {
          await getPreViewUrl(fileId, this.docType, this.permission).then(res => {
            if (res.success) {
              this.docUrl = res.result
            }
          })
        } else {
          await createNewFile().then(res => {
            if (res.success) {
              this.docUrl = res.result
            }
          })
        }
        this.openDoc(this.docUrl)
      },
      /*解决输入框内容影响文档内容*/
      destroyIframe() {
        this.wps.destroy()
      },
      urlChange(url) {
        this.$emit('change', url)
      },
      closed() {
        this.$emit('closed')
      },
      /*手动保存文档*/
      async save() {
        try {
          let result = await this.wps.save()
          console.log(result)
        } catch (e) {
          console.warn(e)
        }
        this.$emit('save')
      },
      loaded() {
        this.loading = false
        console.log('wps doc loaded')
      }
    }
  }
</script>

<style>
  .open-doc {
    margin-left: 15px;
  }

  #viewFile {
    display: flex;
    flex-direction: column;
    padding: 0;
    margin: 0;
    height: 880px;
    /* 防止双击缩放 */
    touch-action: manipulation;
  }
</style>