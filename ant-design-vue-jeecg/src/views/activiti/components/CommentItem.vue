<template>
  <a-comment>
    <template slot="actions">
      <a-form-item v-if="isReplay">
        <a-textarea :rows="4" v-model="value"/>
        <span class="comment-item-btn" @click="handleReplay">回复</span>
        <span class="comment-item-btn" @click="isReplay=false">取消</span>
      </a-form-item>
      <span v-else>
         <span class="comment-item-btn" @click="replay">回复</span>
         <span v-show="comment.createBy===userInfo.username" class="comment-item-btn" @click="deleteReplay">删除</span>
      </span>
    </template>
    <p slot="content">{{comment.content}}</p>
    <a-tooltip slot="datetime" :title="comment.createTime">
      <span>{{ moment(comment.createTime, 'YYYY-MM-DD HH:mm:ss').fromNow() }}</span>
    </a-tooltip>
    <a slot="author">{{comment.author}}</a>
    <a-avatar slot="avatar" :src="comment.avatar"
              alt="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"/>
    <template v-if="comment.children&&comment.children.length>0">
      <comment-item v-for="item in comment.children" :comment="item" @delete="handleDelete"></comment-item>
    </template>
  </a-comment>
</template>

<script>
  import moment from 'moment'
  import { postAction, deleteAction } from '@/api/manage'

  export default {
    name: 'CommentItem',
    props: {
      comment: {
        type: Object,
        default: () => ({ author: '', avatar: '', content: '', createTime: '' })
      }
    },
    data() {
      return {
        isReplay: false,
        value: '',
        moment,
        url: {
          add: '/base/cmsComment/add',
          delete: '/base/cmsComment/delete'
        },
        userInfo: {}
      }
    },
    created() {
      this.userInfo = this.$store.getters.userInfo
    },
    methods: {
      handleDelete(item) {
        if (!this.comment.children) {
          return
        }
        let index = this.comment.children.findIndex(m => m.id === item.id)
        this.comment.children.splice(index, 1)
      },
      deleteReplay() {
        deleteAction(this.url.delete, { id: this.comment.id }).then(res => {
          this.$message.success('删除成功')
          this.$emit('delete', this.comment)
        })
      },
      handleReplay() {
        if (!this.value) {
          return
        }
        let time = moment().format('YYYY-MM-DD HH:mm:ss')
        let item = {
          author: this.userInfo.realname,
          avatar: this.userInfo.avatar,
          content: this.value,
          createBy: this.userInfo.username,
          createTime: time,
          bindId: this.comment.bindId,
          pid: this.comment.id,
          receive: this.comment.createBy,
          title: this.comment.title
        }
        if (!this.comment.children) {
          this.comment.children = []
        }
        postAction(this.url.add, item).then(res => {
          if (res.success) {
            this.comment.children.unshift(item)
            this.value = ''
            this.isReplay = false
          }
        })
      },
      replay() {
        this.isReplay = true
      }
    }
  }
</script>

<style scoped>
  .comment-item-btn {
    cursor: pointer;
    margin-right: 5px;
    font-size: 12px;
    color: grey;
  }
</style>