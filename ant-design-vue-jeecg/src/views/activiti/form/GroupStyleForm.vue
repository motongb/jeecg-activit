<template>
  <div style="margin-top: 20px">
    <GroupStyleFormItem ref="mainForm" :form-id="processForm.tableMetaId" :model="model" :process-form="processForm"
                        @formItemInitial="onFormItemInitial"></GroupStyleFormItem>

    <template v-if="mainFormMeta.tableType===2">
      <a-card>
        <a-tabs :defaultActiveKey="0">
          <template v-for="(subForm,index) in subFormMeta">
            <a-tab-pane forceRender :tab="subForm.description" :key="index">
              <DefaultSubListForm :ref="subForm.currentTableName" :sub-form="subForm" :main-id="model.id"
                                  :is-new="lcModa.isNew"
                                  :data-list="model[subForm.currentTableName]"></DefaultSubListForm>
            </a-tab-pane>
          </template>
        </a-tabs>
      </a-card>
    </template>
  </div>
</template>

<script>
  import { activitiApproveMixin } from '../mixins/activitiApproveMixin'
  import GroupStyleFormItem from '../components/GroupStyleFormItem'
  import DefaultSubListForm from '../components/DefaultSubListForm'

  export default {
    name: 'GroupStyleForm',
    components: { GroupStyleFormItem, DefaultSubListForm },
    mixins: [activitiApproveMixin]
  }
</script>

<style scoped>

</style>