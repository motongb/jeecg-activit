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
      <a-form :form="form">
        <a-row>
          <a-col :span="24">
            <a-form-item label="合同名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['name']" placeholder="请输入合同名称" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="合同编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['code']" placeholder="请输入合同编号" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="类型编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['typeCode', validatorRules.typeCode]" placeholder="请输入类型编码" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="我方" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['firstMember']" placeholder="请输入我方" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="他方" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['secondMember']" placeholder="请输入他方" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="用户id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['userId']" placeholder="请输入用户id" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="合同状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['status']" placeholder="请输入合同状态" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['remark']" placeholder="请输入备注" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: "ContractBaseModal",
    components: { 
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        validatorRules: {
          typeCode: {
            rules: [
              { required: true, message: '请输入类型编码!'},
            ]
          },
        },
        url: {
          add: "/contract/contractBase/add",
          edit: "/contract/contractBase/edit",
        }
     
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','code','typeCode','firstMember','secondMember','userId','status','remark'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'name','code','typeCode','firstMember','secondMember','userId','status','remark'))
      },

      
    }
  }
</script>