<template>
  <a-spin :spinning="confirmLoading">
    <a-form-model ref="ruleForm" :model="form" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-model-item label="标题">
        <a-input v-model="form.title" placeholder="请输入标题"></a-input>
      </a-form-model-item>
      <a-form-model-item label="名称">
        <a-input v-model="form.name" placeholder="请输入名称"></a-input>
      </a-form-model-item>
      <a-form-model-item v-show="!task" label="线路选择">
        <a-checkbox :checked="form.params.condition1==='y'" @change="onChange" value="condition1">路线一</a-checkbox>
        <a-checkbox :checked="form.params.condition2==='y'" @change="onChange" value="condition2">路线二</a-checkbox>
        <a-checkbox :checked="form.params.condition3==='y'" @change="onChange" value="condition3">路线三</a-checkbox>
      </a-form-model-item>
      <a-form-model-item v-if="!disabled" :wrapperCol="{ span: 24 }" style="text-align: center">
        <a-button type="primary" :disabled="disabled||btndisabled" @click="handleSubmit">保存</a-button>
      </a-form-model-item>
    </a-form-model>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import { activitiApproveMixin } from '../../activiti/mixins/activitiApproveMixin'

  export default {
    name: 'TestSignForm',
    components: {
      JFormContainer
    },
    mixins: [activitiApproveMixin],
    props: {},
    data() {
      return {
        form: { params: { condition1: 'y', condition2: 'n', condition3: 'n' } },
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        confirmLoading: false,
        validatorRules: {},
        formParams: {},
        url: {
          add: '/sign/testSign/add',
          edit: '/sign/testSign/edit',
          queryById: '/sign/testSign/queryById'
        }
      }
    },
    computed: {},
    created() {
    },
    methods: {
      onChange(e) {
        if (e.target.checked) {
          this.form.params[e.target.value] = 'y'
        } else {
          this.form.params[e.target.value] = 'n'
        }
      },
      pickValue() {
        this.form.filedNames = 'title,name'
      },
      add() {
        this.edit({})
      },
      edit(record) {
        this.$refs.ruleForm.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.pickValue()
      },
      submitForm() {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            let formData = Object.assign(this.model, values)
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
            })
          }

        })
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'title', 'name'))
      }
    }
  }
</script>