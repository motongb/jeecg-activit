<template>
  <div>
    <a-affix :style="{ position: 'absolute','z-index':10,left:'10px',width:'25%'}">
      <a-card>
        <a-list style="height: 600px;overflow-y: scroll"
          :data-source="comments"
          :header="`${comments.length} ${comments.length > 0 ? '评论' : '无评论'}`"
          item-layout="horizontal">
          <a-list-item slot="renderItem" slot-scope="item, index">
            <comment-item :comment="item"></comment-item>
          </a-list-item>
        </a-list>
        <a-comment>
          <a-avatar
            slot="avatar"
            src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
            alt="Han Solo"/>
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

  export default {
    name: 'CommentModal',
    components: { CommentItem },
    props: {},
    data() {
      return {
        comments: [],
        submitting: false,
        value: '',
        moment
      }
    },
    methods: {
      closed(){
        this.$emit('closed')
      },
      handleSubmit() {
        if (!this.value) {
          return
        }
        this.submitting = true
        setTimeout(() => {
          this.submitting = false
          this.comments = [
            {
              author: 'Han Solo',
              avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
              content: this.value,
              datetime: moment().format('YYYY-MM-DD HH:mm:ss'),
              children:[]
            },
            ...this.comments
          ]
          this.value = ''
        }, 1000)
      },
      handleChange(e) {
        this.value = e.target.value
      }
    }
  }
</script>

<style scoped>

</style>