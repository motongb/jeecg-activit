<template>
  <a-comment :author="comment.author" :avatar="comment.avatar">
    <template slot="actions">
      <a-form-item v-if="isReplay">
        <a-textarea :rows="4" v-model="value"/>
        <span class="comment-item-btn" @click="handleSubmit">回复</span>
        <span class="comment-item-btn" @click="isReplay=false">取消</span>
      </a-form-item>
      <span v-else>
         <span class="comment-item-btn" @click="replay">回复</span>
         <span class="comment-item-btn" @click="replay">删除</span>
      </span>
    </template>
    <p slot="content">{{comment.content}}</p>
    <a-tooltip slot="datetime" :title="comment.datetime">
      <span>{{ moment(comment.datetime, 'YYYY-MM-DD HH:mm:ss').fromNow() }}</span>
    </a-tooltip>
    <template v-if="comment.children&&comment.children.length>0">
      <comment-item v-for="item in comment.children" :comment="item"></comment-item>
    </template>
  </a-comment>
</template>

<script>
  import moment from 'moment'

  export default {
    name: 'CommentItem',
    props: {
      comment: {
        type: Object,
        default: () => ({ author: '', avatar: '', content: '', datetime: '' })
      }
    },
    data() {
      return {
        isReplay: false,
        value: '',
        moment
      }
    },
    methods: {
      handleSubmit() {
        if (!this.value) {
          return
        }
       let time = moment().format('YYYY-MM-DD HH:mm:ss');
        setTimeout(() => {
          this.comment.children.push({
            author: 'Han Solo',
            avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
            content: this.value,
            datetime: time
          })
          this.value = ''
          this.isReplay = false
        }, 1000)
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