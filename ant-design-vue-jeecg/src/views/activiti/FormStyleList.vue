<template>
  <a-row>
    <template v-if="!showForm">
      <a-col v-for="item in list" :span="6">
        <a-card :title="item.title"><a @click="handleClick(item)">{{item.value}}</a></a-card>
      </a-col>
    </template>
    <div v-else>
      <a-button style="margin-bottom: 15px" @click="showForm=false">返回</a-button>
      <component :is="currentForm"></component>
    </div>
  </a-row>
</template>

<script>
  import { getDictItemsFromCache } from '@/api/api'
  import DefaultStyleForm from './form/DefaultStyleForm'

  export default {
    name: 'FormStyleList',
    components: { DefaultStyleForm },
    data() {
      return {
        list: getDictItemsFromCache('act_z_form_style'),
        form: '',
        showForm: false
      }
    },
    computed: {
      currentForm() {
        return this.form
      }
    },
    created() {
    },
    methods: {
      handleClick(item) {
        console.log(item)
        this.form = item.value
        this.showForm = true
      }
    }
  }
</script>

<style scoped>
</style>