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
            <a-form-model-item label="公司代码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyCode">
              <a-input v-model="model.companyCode" placeholder="请输入公司代码"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="公司名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyName">
              <a-input v-model="model.companyName" placeholder="请输入公司名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="供应商代码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supplierCode">
              <a-input v-model="model.supplierCode" placeholder="请输入供应商代码"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="供应商名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supplierName">
              <a-input v-model="model.supplierName" placeholder="请输入供应商名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="采购组织" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="applyDept">
              <a-input v-model="model.applyDept" placeholder="请输入采购组织"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="中标金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amount">
              <a-input v-model="model.amount" placeholder="请输入中标金额"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="programCode">
              <a-input v-model="model.programCode" placeholder="请输入项目编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="programName">
              <a-input v-model="model.programName" placeholder="请输入项目名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="经办人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userId">
              <a-input v-model="model.userId" placeholder="请输入经办人"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="是否需要技术协议" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isProtocol">
              <a-input v-model="model.isProtocol" placeholder="请输入是否需要技术协议"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="中标时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="biddingTime">
              <j-date placeholder="请选择中标时间" v-model="model.biddingTime" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="属性" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="attract">
              <a-input v-model="model.attract" placeholder="请输入属性"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="是否含税" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isRate">
              <a-input v-model="model.isRate" placeholder="请输入是否含税"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="税率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rate">
              <a-input v-model="model.rate" placeholder="请输入税率"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="招标编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="postCode">
              <a-input v-model="model.postCode" placeholder="请输入招标编号"></a-input>
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
    name: 'LgContractBiddingModal',
    components: {},
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
          add: '/contract/lgContractBidding/add',
          edit: '/contract/lgContractBidding/edit'
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