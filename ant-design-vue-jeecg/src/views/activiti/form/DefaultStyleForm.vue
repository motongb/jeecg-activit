<template>
  <div style="margin-top: 20px">
    <a-card>
      <a-spin :spinning="confirmLoading">
        <DefaultActFormItem ref="mainForm" :form-id="processForm.tableMetaId" :model="model"
                            @formItemInitial="onFormItemInitial"></DefaultActFormItem>
        <template v-if="mainFormMeta.tableType===2">
          <a-tabs :defaultActiveKey="0">
            <template v-for="(subForm,index) in subFormMeta">
              <a-tab-pane forceRender :tab="subForm.description" :key="index">
                <DefaultSubListForm :ref="subForm.currentTableName" :sub-form="subForm" :mainData="model"
                                    :is-new="lcModa.isNew" :disabled="lcModa.disabled"/>
              </a-tab-pane>
            </template>
          </a-tabs>
        </template>
      </a-spin>
    </a-card>
  </div>
</template>

<script>
  import { activitiApproveMixin } from '../mixins/activitiApproveMixin'
  import DefaultActFormItem from '../components/DefaultActFormItem'
  import DefaultSubListForm from '../components/DefaultSubListForm'

  export default {
    name: 'DefaultStyleForm',
    mixins: [activitiApproveMixin],
    components: { DefaultSubListForm, DefaultActFormItem },
    props: {},
    data() {
      return {}
    },
    created() {
      //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model))
    },
    methods: {}
  }
</script>

<style scoped>

</style>