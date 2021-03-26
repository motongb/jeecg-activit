<template>
  <div>
    <a-affix :style="{ position: 'absolute','z-index':10,left:'10px',width:'25%'}">
      <a-card>
        <a-list style="height: 600px;overflow-y: scroll"
                :data-source="comments"
                :header="`${comments.length} ${comments.length > 0 ? '评论' : '无评论'}`"
                item-layout="horizontal">
          <a-list-item slot="renderItem" slot-scope="item, index">
            <comment-item :comment="item" @delete="handleDelete"></comment-item>
          </a-list-item>
        </a-list>
        <a-comment>
          <a-avatar slot="avatar" :src="userInfo.avatar"
                    alt="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"/>
          <div slot="content">
            <a-form-item>
              <a-textarea :rows="4" :value="value" @change="handleChange"/>
            </a-form-item>
            <a-form-item>
              <a-button html-type="submit" :loading="submitting" type="primary" @click="handleSubmit">发 表</a-button>
              <a-button style="margin-left: 8px" type="primary" ghost @click="closed">关 闭</a-button>
            </a-form-item>
          </div>
        </a-comment>
      </a-card>
    </a-affix>
  </div>
</template>

<script>
  import moment from 'moment'
  import CommentItem from './CommentItem'
  import { postAction, getAction, deleteAction } from '@/api/manage'

  export default {
    name: 'CommentModal',
    components: { CommentItem },
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
      }
    },
    data() {
      return {
        comments: [],
        submitting: false,
        value: '',
        moment,
        url: {
          add: '/base/cmsComment/add',
          tree: '/base/cmsComment/tree',
          delete: '/base/cmsComment/delete'
        },
        userInfo: {}
      }
    },
    created() {
      this.getValue()
      this.userInfo = this.$store.getters.userInfo
    },
    methods: {
      handleDelete(item) {
        let index = this.comments.findIndex(m => m.id === item.id)
        this.comments.splice(index, 1)
      },
      getValue() {
        getAction(this.url.tree, { bindId: this.bindId }).then(res => {
          this.comments = res.result
        })
      },
      closed() {
        this.$emit('closed')
      },
      handleSubmit() {
        if (!this.value) {
          return
        }
        this.submitting = true
        let item = {
          author: this.userInfo.realname,
          avatar: this.userInfo.avatar,
          content: this.value,
          createTime: moment().format('YYYY-MM-DD HH:mm:ss'),
          children: [],
          bindId: this.bindId,
          receive: this.bindUser,
          title: this.title
        }
        postAction(this.url.add, item).then(res => {
          if (res.success) {
            this.comments.unshift(item)
            this.value = ''
            this.$message.success('评论成功')
          }
          this.submitting = false
        }).catch(_ => this.submitting = false)
      },
      handleChange(e) {
        this.value = e.target.value
      }
    }
  }
</script>

<style scoped>

</style>