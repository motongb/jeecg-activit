<template>
  <!--流程业务表单-->
  <div class="form-main">
    <a-card class="apply-card">
      <a-form-model ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="名称">
              <a-input :disabled="disabled" v-model="form.name" placeholder="给目标起个名字"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-model-item v-show="!task" label="线路选择">
              <a-radio-group v-model="form.params.condition">
                <a-radio value="路线一">路线一</a-radio>
                <a-radio value="路线二">路线二</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
  </div>
</template>

<script>
  import { activitiApproveMixin } from '../mixins/activitiApproveMixin'
  import pick from 'lodash.pick'

  export default {
    name: 'demoForm',
    mixins: [activitiApproveMixin],
    data() {
      return {
        labelCol: { span: 2 },
        wrapperCol: { span: 16 },
        description: '流程表单demo，按例开发表单。需在 activitiMixin.js 中加入写好的表单',
        // form
        form: { params: { condition: '路线一' } }
      }
    },
    created() {
    },
    methods: {
      // 实现activitiApproveMixin方法,params,id,tableName必须设置
      pickValue() {
        // 需要保存的字段
        this.form.filedNames = 'name'
      }
    }
  }
</script>
<style lang="less" scoped>
  .form-main {
  }
  .apply-card{
    margin-bottom: 24px;
  }
</style>