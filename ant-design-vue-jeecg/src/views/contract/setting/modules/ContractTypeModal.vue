<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    :destroyOnClose="true"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item label="父级节点" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-tree-select
            ref="treeSelect"
            placeholder="请选择父级节点"
            v-decorator="['pid']"
            dict="contract_type,name,id"
            pidField="pid"
            pidValue="0"
            hasChildField="has_child"
          >
          </j-tree-select>
        </a-form-item>
        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['name', validatorRules.name]" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['code', validatorRules.code]" placeholder="请输入编码"></a-input>
        </a-form-item>
        <a-form-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['sort']" placeholder="请输入排序"></a-input>
        </a-form-item>
        <a-form-item label="流程定义" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-tree-select
            v-decorator="['processDef',{initialValue:form.processDef}]"
            allow-clear
            treeNodeFilterProp="title"
            show-search
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="options"
            placeholder="Please select">
          </a-tree-select>
          <!--          <a-cascader v-decorator="['processDef']" :options="options" :show-search="{ filter }"-->
          <!--                      placeholder="Please select" @change="onChange"/>-->
        </a-form-item>

      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { initDictOptions } from '@/components/dict/JDictSelectUtil'
  import JTreeSelect from '@/components/jeecg/JTreeSelect'

  export default {
    name: 'ContractTypeModal',
    components: {
      JTreeSelect
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: '操作',
        width: 800,
        visible: false,
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
        validatorRules: {
          name: {
            rules: [
              { required: true, message: '请输入名称!' }
            ]
          },
          code: {
            rules: [
              { required: true, message: '请输入编码!' }
            ]
          }
        },
        url: {
          add: '/contract/contractType/add',
          edit: '/contract/contractType/edit',
          getProcessDataList: '/activiti_process/listData'
        },
        expandedRowKeys: [],
        pidField: 'pid',
        options: []
      }
    },
    created() {
      initDictOptions('bpm_process_type').then(res => {
        if (res.success) {
          this.options = res.result.map(item => ({
            title: item.title,
            value: item.value,
            key: item.value,
            children: []
          }))
          this.getProcess()
        }
      })
    },
    methods: {
      getProcess() {
        this.getAction(this.url.getProcessDataList, { status: 1, roles: true, zx: true }).then(res => {
          if (res.success) {
            this.options.forEach(option => {
              let temp = res.result.filter(c => c.categoryId === option.value)
              if (temp) {
                option.children = temp.map(m => ({ title: m.name, value: m.processKey, key: m.processKey }))
              }
            })
            console.log(this.options)
          }
        })
      },
      onChange(value, selectedOptions) {
        console.log(value, selectedOptions)
        this.form.setFieldsValue({ processDef: selectedOptions })
      },
      add(obj) {
        this.edit(obj)
      },
      edit(record) {
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'pid', 'name', 'code', 'sort', 'processDef'))
        })
      },
      close() {
        this.$emit('close')
        this.visible = false
      },
      handleOk() {
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
            let old_pid = this.model[this.pidField]
            let formData = Object.assign(this.model, values)
            let new_pid = this.model[this.pidField]
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                this.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })
          }

        })
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'pid', 'name', 'code', 'sort', 'processDef'))
      },
      submitSuccess(formData, flag) {
        if (!formData.id) {
          let treeData = this.$refs.treeSelect.getCurrTreeData()
          this.expandedRowKeys = []
          this.getExpandKeysByPid(formData[this.pidField], treeData, treeData)
          this.$emit('ok', formData, this.expandedRowKeys.reverse())
        } else {
          this.$emit('ok', formData, flag)
        }
      },
      getExpandKeysByPid(pid, arr, all) {
        if (pid && arr && arr.length > 0) {
          for (let i = 0; i < arr.length; i++) {
            if (arr[i].key == pid) {
              this.expandedRowKeys.push(arr[i].key)
              this.getExpandKeysByPid(arr[i]['parentId'], all, all)
            } else {
              this.getExpandKeysByPid(pid, arr[i].children, all)
            }
          }
        }
      }


    }
  }
</script>