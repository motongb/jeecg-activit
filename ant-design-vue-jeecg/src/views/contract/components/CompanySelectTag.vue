<template>
  <a-auto-complete :disabled="disabled" v-model="text" :data-source="dataSource" optionLabelProp="label" style="width: 100%"
                   @select="onSelect" @search="onSearch">
    <template slot="dataSource">
      <a-select-option v-for="(item,index) in dataSource" :key="index" :value="item.id" :label="item.nameCn">
        {{ item.nameCn }}
      </a-select-option>
    </template>
    <a-input>
      <a-icon slot="suffix" type="search"/>
    </a-input>
  </a-auto-complete>
</template>

<script>
  import { getAction } from '@/api/manage'

  export default {
    name: 'CompanySelectTag',
    model: {
      prop: 'value',
      event: 'change'
    },
    props: {
      disabled:{
        type:Boolean,
        default:false
      },
      value: {
        type: String,
        default: undefined
      }
    },
    data() {
      return {
        text: '',
        dataSource: [],
        isLoading: false,
        url: {
          list: '/contract/company/list',
          queryById: '/contract/company/queryById'
        }

      }
    },
    watch: {
      value() {
        if (this.value) {
          this.queryById()
        }
      }
    },
    created() {
      if (this.value) {
        this.queryById()
      }
    },
    methods: {
      onSearch(searchText) {
        if (!this.isLoading) {
          this.isLoading = !this.isLoading
          this.query(searchText)
        }
      },
      queryById() {
        getAction(this.url.queryById, { id: this.value }).then(res => {
          if (res.success) {
            this.text = res.result.nameCn
          }
        })
      },
      query(searchText) {
        getAction(this.url.list, { nameCn: searchText }).then(res => {
          this.isLoading = !this.isLoading
          if (res.success) {
            this.dataSource = res.result.records
          }
        }).catch(() => this.isLoading = !this.isLoading)
      },
      onSelect(value) {
        console.log('onSelect', value)
        this.$emit('change', value)
        this.$emit('select', this.dataSource.filter(item => item.id === value)[0])
      }
    }
  }
</script>

<style scoped>

</style>