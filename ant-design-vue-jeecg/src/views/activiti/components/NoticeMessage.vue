<template>
  <div>
    <a-modal title="修改通知" :visible="modalTaskVisible" @cancel="cancel" :width="600">
      <a-form ref="form" :model="form" :label-width="85">
        <a-form-item label="修改建议" prop="advice">
          <a-input type="textarea" v-model="form.comment" :rows="4"/>
        </a-form-item>
      </a-form>
      <div slot="footer">
        <a-button type="text" @click="cancel">取消</a-button>
        <a-button type="primary" :loading="submitLoading" @click="handelSubmit">提交</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
  import moment from 'moment'
  import { getAction, postAction } from '@/api/manage'

  export default {
    name: 'NoticeMessage',
    props: {
      bindId: {
        type: String,
        default: ''
      },
      bindUser: {
        type: String,
        default: ''
      },
      title: {
        type: String,
        default: ''
      },
      businessKey: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        modalTaskVisible: false,
        submitLoading: false,
        form: { comment: '' },
        userInfo: this.$store.getters.userInfo,
        url: {
          updateFix: '/actBusiness/updateFix',
          add: '/base/cmsComment/add'
        }
      }
    },
    methods: {
      show() {
        this.modalTaskVisible = true
      },
      cancel() {
        this.modalTaskVisible = false
        this.submitLoading = false
      },
      handelSubmit() {
        let item = {
          author: this.userInfo.realname,
          avatar: this.userInfo.avatar,
          content: this.form.comment,
          createTime: moment().format('YYYY-MM-DD HH:mm:ss'),
          children: [],
          bindId: this.bindId,
          receive: this.bindUser,
          title: this.title + '-修改通知'
        }
        this.submitLoading = true
        postAction(this.url.add, item).then(res => {
          if (res.success) {
            getAction(this.url.updateFix, { id: this.businessKey, status: 1 }).then(res => {
              this.$message.success('提交成功')
              this.cancel()
              this.form.comment = ''
            })
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>