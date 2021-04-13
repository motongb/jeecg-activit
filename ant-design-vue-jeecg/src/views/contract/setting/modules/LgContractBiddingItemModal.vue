<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="采购计划编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="planId">
              <a-input v-model="model.planId" placeholder="请输入采购计划编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="物料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemName">
              <a-input v-model="model.itemName" placeholder="请输入物料名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="物料编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemCode">
              <a-input v-model="model.itemCode" placeholder="请输入物料编码"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unit">
              <a-input v-model="model.unit" placeholder="请输入计量单位"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="物料组" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="groups">
              <a-input v-model="model.groups" placeholder="请输入物料组"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="count">
              <a-input-number v-model="model.count" placeholder="请输入数量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cost">
              <a-input v-model="model.cost" placeholder="请输入单价"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amount">
              <a-input v-model="model.amount" placeholder="请输入金额"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="价格单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="costUnit">
              <a-input v-model="model.costUnit" placeholder="请输入价格单位"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="交货期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deadline">
              <j-date placeholder="请选择交货期" v-model="model.deadline" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="订货依据及参数要求" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="standard">
              <a-input v-model="model.standard" placeholder="请输入订货依据及参数要求"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="品牌" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="brand">
              <a-input v-model="model.brand" placeholder="请输入品牌"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="申报单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="applyDept">
              <a-input v-model="model.applyDept" placeholder="请输入申报单位"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="申报人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="applyPerson">
              <a-input v-model="model.applyPerson" placeholder="请输入申报人"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="需求工厂" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="factory">
              <a-input v-model="model.factory" placeholder="请输入需求工厂"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'LgContractBiddingItemModal',
    components: {},
    props: {
      mainId: {
        type: String,
        required: false,
        default: ''
      }
    },
    data() {
      return {
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
        validatorRules: {},
        url: {
          add: '/contract/lgContractBidding/addLgContractBiddingItem',
          edit: '/contract/lgContractBidding/editLgContractBiddingItem'
        }

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
      close() {
        this.$emit('close')
        this.visible = false
        this.$refs.form.clearValidate()
      },
      handleOk() {
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
            this.model['biddingId'] = this.mainId
            httpAction(httpurl, this.model, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })
          } else {
            return false
          }
        })
      },
      handleCancel() {
        this.close()
      }


    }
  }
</script>
