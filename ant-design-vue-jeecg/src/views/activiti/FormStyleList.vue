<template>
  <a-row>
    <template v-if="!showForm">
      <a-col v-for="item in list" :span="6">
        <a-card :title="item.title"><a @click="handleClick(item)">{{item.value}}</a></a-card>
      </a-col>
    </template>
    <div v-else>
      <a-button style="margin-bottom: 15px" @click="showForm=false">返回</a-button>
      <component :is="currentForm" :lcModa="lcModa"></component>
    </div>
  </a-row>
</template>

<script>
  import { getDictItemsFromCache } from '@/api/api'
  import StyleForm from './form'

  export default {
    name: 'FormStyleList',
    components: StyleForm,
    data() {
      return {
        list: getDictItemsFromCache('act_z_form_style'),
        form: '',
        showForm: false,
        lcModa: {
          dept: '', //部门
          title: '',//标题
          disabled: false,// 全局禁用
          processData: {},//流程数据
          isNew: false,//是否新增
          isTask: false//是否处理流程
        }
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
        let formCode = ''
        switch (item.value) {
          case 'DefaultStyleForm':
            formCode = 'C001'
            break
          case 'GroupStyleForm':
            formCode = 'C002'
            break
          default:
            break
        }
        this.lcModa.processData.formCode = formCode
        this.form = item.value
        this.showForm = true
      }
    }
  }
</script>

<style scoped>
</style>