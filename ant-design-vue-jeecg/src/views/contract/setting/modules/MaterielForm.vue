<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="text">
              <a-input v-model="model.text" placeholder="请输入描述"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="长描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="textLong">
              <a-input v-model="model.textLong" placeholder="请输入长描述"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
              <a-input v-model="model.code" placeholder="请输入编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="category">
              <j-dict-select-tag v-model="model.category" placeholder="请输入分类" dictCode="materiel_category"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="物料组" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="groups">
              <a-input v-model="model.groups" placeholder="请输入物料组"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="物料组描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="groupsName">
              <a-input v-model="model.groupsName" placeholder="请输入物料组描述"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="基本单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unit">
              <j-dict-select-tag v-model="model.unit" placeholder="请输入基本单位" dictCode="materiel_unit"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="大小/量纲" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="size">
              <a-input v-model="model.size" placeholder="请输入大小/量纲"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="毛重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="grossWeight">
              <a-input v-model="model.grossWeight" placeholder="请输入毛重"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="净重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="netWeight">
              <a-input v-model="model.netWeight" placeholder="请输入净重"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="重量单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unitWeight">
              <j-dict-select-tag v-model="model.unitWeight" placeholder="请输入重量单位" dictCode="materiel_unit"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="体积单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="volume">
              <j-dict-select-tag v-model="model.volume" placeholder="请输入体积单位" dictCode="materiel_unit"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="行业标准描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="standard">
              <a-input v-model="model.standard" placeholder="请输入行业标准描述"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'MaterielForm',
    components: {},
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data() {
      return {
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
        url: {
          add: '/contract/materiel/add',
          edit: '/contract/materiel/edit',
          queryById: '/contract/materiel/queryById'
        }
      }
    },
    computed: {
      formDisabled() {
        return this.disabled
      }
    },
    created() {
      //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model))
    },
    methods: {
      add() {
        this.edit(this.modelDefault)
      },
      edit(record) {
        this.model = Object.assign({}, record)
        this.visible = true
      },
      submitForm() {
        const that = this
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            httpAction(httpurl, this.model, method).then((res) => {
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
      }
    }
  }
</script>