<template>
  <div>
    <a-button type="primary" class="editable-add-btn" @click="handleAdd">字段组</a-button>
    <a-table ref="table"
             size="middle"
             bordered
             rowKey="id"
             :scroll="{x:true}"
             :pagination="false"
             :columns="columns"
             :dataSource="groupData">
      <template slot="action" slot-scope="text, record">
        <a @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical"/>
        <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
          <a>删除</a>
        </a-popconfirm>
      </template>
    </a-table>

    <a-modal
      :width="800"
      title="分组"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel">
      <div>
        <a-form-model ref="form" :model="model" :rules="validatorRules">
          <a-row>
            <a-col :span="24">
              <a-form-model-item label="组名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="groupName">
                <a-input v-model="model.groupName" placeholder="请输入组名"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sort">
                <a-input v-model="model.sort" placeholder="请输入排序"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item>
                <a-transfer style="margin-left: 120px" :list-style="{ width: '250px', height: '400px'}"
                            :data-source="fieldData" show-search :filter-option="filterOption"
                            :target-keys="targetKeys" :render="item => item.title" @change="handleChange"/>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </div>
    </a-modal>
  </div>
</template>

<script>
  import { httpAction, getAction, deleteAction } from '@/api/manage'
  import { randomUUID, cloneObject } from '@/utils/util'

  export default {
    name: 'ActZFieldGroup',
    props: {
      tableId: {
        type: String,
        default: null
      },
      dataId: {
        type: String,
        default: null
      }
    },
    watch: {
      tableId: {
        immediate: true,
        handler() {
          if (this.tableId) {
            this.getFieldList()
          }
        }
      },
      dataId: {
        immediate: true,
        handler() {
          if (this.dataId) {
            this.getGroupData()
          }
        }
      }
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 80,
            align: 'center',
            customRender: function(t, r, index) {
              return parseInt(index) + 1
            }
          },
          {
            title: '组名',
            align: 'center',
            dataIndex: 'groupName'
          },
          {
            title: '字段列表',
            align: 'center',
            dataIndex: 'fieldList'
          },
          {
            title: '排序',
            align: 'center',
            dataIndex: 'sort'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            fixed: 'right',
            width: 147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          getGroupData: '/process/actZForm/queryActZFieldGroupByMainId',
          fieldList: '/online/cgform/field/listByHeadId',
          fieldGroup: '/process/actZForm/actZFieldGroup'
        },
        validatorRules: {
          groupName: [{ required: true, message: '请输入组名' }]
        },
        confirmLoading: false,
        groupData: [],
        visible: false,
        model: {},
        targetKeys: [],
        fieldData: [],
        fieldDataBackup: []
      }
    },
    created() {

    },
    methods: {
      getFieldList() {
        getAction(this.url.fieldList, { headId: this.tableId }).then(res => {
          this.fieldDataBackup = res.result.map(m => ({
            key: m.dbFieldName,
            title: m.dbFieldTxt,
            description: m.dbFieldTxt
          }))
        })
      },
      filterOption(inputValue, option) {
        return option.description.indexOf(inputValue) > -1
      },
      handleChange(targetKeys, direction, moveKeys) {
        this.targetKeys = targetKeys
      },
      handleOk() {
        this.confirmLoading = true
        let that = this
        this.$refs.form.validate(valid => {
          if (valid) {
            that.model.fieldList = that.targetKeys.join(',')
            that.model.formId = that.dataId
            let methods = ''
            if (that.model.id) {
              methods = 'put'
            } else {
              methods = 'post'
            }
            httpAction(that.url.fieldGroup, that.model, methods).then(res => {
              if (res.success) {
                that.handleCancel()
                that.getGroupData()
              }
              that.confirmLoading = false
            })
          }
        })
      },
      handleCancel() {
        this.visible = false
        this.model = {}
      },
      /*添加组*/
      handleAdd() {
        if (!this.tableId) {
          this.$message.warning('请选择数据库表')
          return
        }
        this.targetKeys = []
        this.handleEdit({})
      },
      /*编辑*/
      handleEdit(record) {
        let groupData = cloneObject(this.groupData)
        this.model = Object.assign({}, record)
        this.visible = true
        if (this.model.id) {
          this.targetKeys = this.model.fieldList.split(',')
          groupData = groupData.filter(m => m.id !== this.model.id)
        }
        let fields = groupData.map(m => m.fieldList).join(',')
        this.fieldData = this.fieldDataBackup.filter(m => fields.indexOf(m.key) === -1)
      },
      /*查询组*/
      getGroupData() {
        getAction(this.url.getGroupData, { id: this.dataId }).then(res => {
          this.groupData = res.result
        })
      },
      handleDelete(id) {
        deleteAction(this.url.fieldGroup, { ids: id }).then(res => {
          if (res.success) {
            this.$message.success('删除成功')
            this.getGroupData()
          }
        })
      }
    }
  }
</script>

<style scoped>
  .editable-add-btn {
    margin-bottom: 8px;
  }
</style>