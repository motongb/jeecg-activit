<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="合同名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['name']" placeholder="请输入合同名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="合同编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['code']" placeholder="请输入合同编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="类型编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['typeCode']" placeholder="请输入类型编码"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="我方" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['firstMember']" placeholder="请输入我方"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="乙方" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['secondMember']" placeholder="请输入乙方"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="丙方" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['thirdMember']" placeholder="请输入丙方"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="用户id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['userId']" placeholder="请输入用户id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['status']" placeholder="请输入状态"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['remark']" placeholder="请输入备注"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="签署方数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['memberUse']" placeholder="请输入签署方数"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'

  export default {
    name: 'ContractPurchaseForm',
    components: {
      JFormContainer,
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        form: this.$form.createForm(this),
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
        },
        url: {
          add: "/contract/contractPurchase/add",
          edit: "/contract/contractPurchase/edit",
          queryById: "/contract/contractPurchase/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
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
          this.form.setFieldsValue(pick(this.model,'name','code','typeCode','firstMember','secondMember','thirdMember','userId','status','remark','memberUse'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
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
            })
          }
         
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'name','code','typeCode','firstMember','secondMember','thirdMember','userId','status','remark','memberUse'))
      },
    }
  }
</script>