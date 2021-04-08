<template>
  <div>
    <div v-dragging v-show="visible" class="p-modal">
      <a-card style="width: 100%">
        <a-button @click="visible=false" style="margin-bottom: 0;">关闭</a-button>
        <a-table :pagination="false" rowKey="id" :columns="paramsColumns" :data-source="paramsData">
        </a-table>
      </a-card>
    </div>
    <a href="javascript:void(0);" class="view-params-btn" @click="visible=true">查看可用参数</a>
    <wps-view-tag ref="wpsView" :showSaveBtn="true" @closed="closed" @fileOpen="fileOpenCallback"></wps-view-tag>
  </div>
</template>

<script>
  import WpsViewTag from './WpsViewTag'
  import { putAction, getAction } from '@/api/manage'
  import { filterDictTextByDictCode } from '@comp/dict/JDictSelectUtil'
  import '@/utils/directive/dragging'

  export default {
    name: 'ModelEditPage',
    components: { WpsViewTag },
    data() {
      return {
        fileId: '',
        modelId: '',
        visible: false,
        url: {
          edit: '/contract/contractModel/edit',
          queryById: '/contract/contractModel/queryById'
        },
        paramsColumns: [
          {
            title: '字段名称',
            align: 'left',
            dataIndex: 'name'
          },
          {
            title: '字段key',
            align: 'left',
            dataIndex: 'fieldKey'
          },
          {
            title: '类型',
            align: 'left',
            dataIndex: 'type',
            customRender: function(t, r, index) {
              return filterDictTextByDictCode('contract_field_type', t)
            }
          },
          {
            title: '列表索引',
            align: 'left',
            dataIndex: 'tableIndex'
          }
        ],
        paramsData: []
      }
    },
    mounted() {
      let params = this.$route.query
      console.log(params)
      this.fileId = params.fileId
      this.modelId = params.id
      this.$refs.wpsView.init(params.fileId)
      getAction(this.url.queryById, { id: params.id }).then(res => {
        if (res.result.paramsFields) {
          this.paramsData = JSON.parse(res.result.paramsFields)
        }
      })
    },
    methods: {
      handleOk() {
        this.visible = false
      },
      fileOpenCallback(file) {
        if (file) {
          this.fileId = file.id.split('-')[1]
          putAction(this.url.edit, { id: this.modelId, fileId: this.fileId }).then(res => {
            if (res.success) {
            }
          })
        }
        console.log(this.fileId)
      },
      closed() {
        this.$router.push('/contract/setting/model')
        this.$bus.$emit('closed-current-tabs')
      }
    }
  }
</script>

<style scoped>
  .p-modal {
    position: absolute;
    top: 20%;
    left: 0;
    z-index: 999;
  }
  .view-params-btn{
    position: absolute;
    z-index: 1;
    left: 22%;
  }
</style>