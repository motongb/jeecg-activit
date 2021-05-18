<template>
  <div>
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <template v-for="group in groupList">
          <a-card class="apply-card" :title="group.name">
            <a-row>
              <template v-for="field in group.fieldList">
                <a-col :span="12" v-if="properties[field].view===FormItemType.text">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-input v-model="model[field]" :placeholder="`请输入${properties[field].title}`"></a-input>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.password">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-input-password v-model="model[field]" :placeholder="`请输入${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.list">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-select v-model="model[field]"
                              :placeholder="`请选择${properties[field].title}`">
                      <a-select-option v-for="option in properties[field].enum" :key="option.value">{{ option.text }}
                      </a-select-option>
                    </a-select>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.radio">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-radio-group v-model="model[field]">
                      <a-radio v-for="item in properties[field].enum"
                               :value="properties[field].type==='number'?Number(item.value):item.value">{{item.text}}
                      </a-radio>
                    </a-radio-group>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.checkbox">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-multi-select-tag type="checkbox" v-model="model[field]" :options="properties[field].enum"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.switch">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-switch v-model="model[field]"
                              :options="properties[field].extendOption?properties[field].extendOption:['Y','N']"></j-switch>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.date">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-date :placeholder="`请选择${properties[field].title}`" v-model="model[field]" style="width: 100%"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.time">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-time :placeholder="`请选择${properties[field].title}`" v-model="model[field]" style="width: 100%"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.file">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-upload v-model="model.file"></j-upload>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.image">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-image-upload isMultiple v-model="model[field]"></j-image-upload>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.textarea">
                  <a-form-model-item :label="properties[field].title"
                                     :labelCol="{xs:{span:24},sm:{span:2}}" :wrapperCol="{xs:{span:24},sm:{span:20}}"
                                     :prop="field">
                    <a-textarea v-model="model[field]" rows="4" :placeholder="`请输入${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.list_multi">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-multi-select-tag type="list_multi" v-model="model[field]" :options="properties[field].enum"
                                        :placeholder="`请选择${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.sel_search">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-search-select-tag v-model="model[field]" :async="properties[field].dictTable.length>0"
                                         :dict="properties[field].dictTable.length>0?`${properties[field].dictTable},${properties[field].dictText},${properties[field].dictCode}`:properties[field].dictCode"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.popup">
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
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.cat_tree">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-category-select v-model="model[field]" :pid="properties[field].pidValue"
                                       :placeholder="`请选择${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.sel_depart">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-select-depart v-model="model[field]" multi/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.sel_user">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-select-user-by-dep v-model="model[field]"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.umeditor">
                  <a-form-model-item :label="properties[field].title" :labelCol="{xs:{span:24},sm:{span:2}}"
                                     :wrapperCol="{xs:{span:24},sm:{span:20}}" :prop="field">
                    <j-editor v-model="model[field]"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="24" v-else-if="properties[field].view===FormItemType.markdown">
                  <a-form-model-item :label="properties[field].title" :labelCol="{xs:{span:24},sm:{span:2}}"
                                     :wrapperCol="{xs:{span:24},sm:{span:20}}"
                                     :prop="field">
                    <j-markdown-editor v-model="model[field]" id="markdown"></j-markdown-editor>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.pca">
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <j-area-linkage type="cascader" v-model="model[field]"
                                    :placeholder="`请输入${properties[field].title}`"/>
                  </a-form-model-item>
                </a-col>
                <a-col :span="12" v-else-if="properties[field].view===FormItemType.sel_tree">
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
                <a-col :span="12" v-else>
                  <a-form-model-item :label="properties[field].title" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                     :prop="field">
                    <a-input v-model="model[field]" :placeholder="`请输入${properties[field].title}`"></a-input>
                  </a-form-model-item>
                </a-col>
              </template>
            </a-row>
          </a-card>
        </template>
      </a-form-model>
    </j-form-container>
  </div>
</template>

<script>
  import { styleFormMixin } from '../mixins/styleFormMixin'

  export default {
    name: 'GroupStyleFormItem',
    mixins: [styleFormMixin],
    props: {
      processForm: {
        type: Object,
        default: () => ({})
      }
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        }
      }
    },
    computed: {
      groupList() {
        let fieldGroups = this.processForm.actZFieldGroupList
        if (!fieldGroups || fieldGroups.length < 1) {
          return []
        }
        let groups = []
        for (let item of fieldGroups) {
          let fieldList = this.fieldList.filter(m => item.fieldList.indexOf(m) > -1)
          groups.push({ name: item.groupName, fieldList })
        }
        return groups
      }
    },
    created() {
    },
    methods: {}
  }
</script>

<style scoped>
  .apply-card {
    margin-bottom: 8px;
  }
</style>