<template>
  <div style="margin-top: 20px">
    <a-card>
      <a-spin :spinning="confirmLoading">
        <j-form-container :disabled="formDisabled">
          <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
            <a-row>
              <template v-for="field in fieldList">
                <a-col :span="24" v-if="properties[field].view===FormItemType.text">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-input v-model="model[field]" :placeholder="`请输入${properties[field].title}`"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.password">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-input-password v-model="model[field]" :placeholder="`请输入${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.list">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-select v-model="model[field]"
                              :placeholder="`请选择${properties[field].title}`">
                      <a-select-option v-for="option in properties[field].enum" :key="option.value">{{ option.text }}
                      </a-select-option>
                    </a-select>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.radio">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-radio-group v-model="model[field]">
                      <a-radio v-for="item in properties[field].enum" :value="item.value">{{item.text}}</a-radio>
                    </a-radio-group>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.checkbox">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-multi-select-tag type="checkbox" v-model="model[field]" :options="properties[field].enum"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.switch">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-switch v-model="model[field]"
                              :options="properties[field].extendOption?properties[field].extendOption:['Y','N']"></j-switch>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.date">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-date :placeholder="`请选择${properties[field].title}`" v-model="model[field]" style="width: 100%"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.time">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-time :placeholder="`请选择${properties[field].title}`" v-model="model[field]" style="width: 100%"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.file">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-upload v-model="model.file"></j-upload>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.image">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-image-upload isMultiple v-model="model[field]"></j-image-upload>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.textarea">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-textarea v-model="model[field]" rows="4" :placeholder="`请输入${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.list_multi">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-multi-select-tag type="list_multi" v-model="model[field]" :options="properties[field].enum"
                                        :placeholder="`请选择${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.sel_search">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-search-select-tag v-model="model[field]" :async="properties[field].dictTable.length>0"
                                         :dict="properties[field].dictTable.length>0?`${properties[field].dictTable},${properties[field].dictText},${properties[field].dictCode}`:properties[field].dictCode"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.popup">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-popup
                      v-model="model[field]"
                      :field="field"
                      :org-fields="properties[field].orgFields"
                      :dest-fields="properties[field].destFields"
                      :code="properties[field].code"
                      @input="popupCallback"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.cat_tree">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-category-select v-model="model[field]" :pid="properties[field].pidValue"
                                       :placeholder="`请选择${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.sel_depart">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-select-depart v-model="model[field]" multi/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.sel_user">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-select-user-by-dep v-model="model[field]"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.umeditor">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-editor v-model="model[field]"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.markdown">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-markdown-editor v-model="model[field]" id="markdown"></j-markdown-editor>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.pca">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-area-linkage type="cascader" v-model="model[field]"
                                    :placeholder="`请输入${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.sel_tree">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-tree-select
                      ref="treeSelect"
                      :placeholder="`请选择${properties[field].title}`"
                      v-model="model[field]"
                      :dict="properties[field].dict"
                      :pidField="properties[field].pidField"
                      :hasChildField="properties[field].hasChildField"
                      :pidValue="properties[field].pidValue">
                    </j-tree-select>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else>
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-input v-model="model[field]" :placeholder="`请输入${properties[field].title}`"></a-input>
                  </a-form-model-item>
                </a-col>
              </template>
            </a-row>
          </a-form-model>
        </j-form-container>
      </a-spin>
    </a-card>
  </div>
</template>

<script>
  import { activitiApproveMixin } from '../mixins/activitiApproveMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import { getAction, postAction, httpAction } from '@/api/manage'


  export default {
    name: 'DefaultStyleForm',
    mixins: [activitiApproveMixin],
    props: {},
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        }
      }
    },
    computed: {
      formDisabled() {
        return this.lcModa.disabled
      }
    },
    created() {
      //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model))
    },
    methods: {
      // add() {
      //   this.edit(this.modelDefault)
      // },
      // edit(record) {
      //   this.model = Object.assign({}, record)
      //   this.visible = true
      // },
      // submitForm() {
      //   const that = this
      //   // 触发表单验证
      //   this.$refs.form.validate(valid => {
      //     if (valid) {
      //       that.confirmLoading = true
      //       let httpurl = ''
      //       let method = ''
      //       if (!this.model.id) {
      //         httpurl += this.url.add
      //         method = 'post'
      //       } else {
      //         httpurl += this.url.edit
      //         method = 'put'
      //       }
      //       httpAction(httpurl, this.model, method).then((res) => {
      //         if (res.success) {
      //           that.$message.success(res.message)
      //           that.$emit('ok')
      //         } else {
      //           that.$message.warning(res.message)
      //         }
      //       }).finally(() => {
      //         that.confirmLoading = false
      //       })
      //     }
      //
      //   })
      // },
      popupCallback(value, row) {
        this.model = Object.assign(this.model, row)
      },
      handleCategoryChange(value, backObj) {
        this.model = Object.assign(this.model, backObj)
      }
    }
  }
</script>

<style scoped>

</style>