<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-item label="项目相关合同" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['relateProject']" placeholder="请输入项目相关合同" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="相关项目" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['project']" placeholder="请输入相关项目"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="是否子合同" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['isSub']" placeholder="请输入是否子合同" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="主合同id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['parentId']" placeholder="请输入主合同id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="预算相关合同" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['budget']" placeholder="请输入预算相关合同" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="成本中心" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['costCenter']" placeholder="请输入成本中心"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="密级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['isSecret']" placeholder="请输入密级" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择开始时间" v-decorator="['startTime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择结束时间" v-decorator="['endTime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="采购方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['purchaseType']" placeholder="请输入采购方式" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="招标编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['biddingId']" placeholder="请输入招标编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="相关框架协议" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['protocol']" placeholder="请输入相关框架协议"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="采购订单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['purchaseId']" placeholder="请输入采购订单号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="合同金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['amount']" placeholder="请输入合同金额"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="合同金额大写" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['amountLarge']" placeholder="请输入合同金额大写"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="模板id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['modelId']" placeholder="请输入模板id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="合同影像文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['filePdf']" placeholder="请输入合同影像文件"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['fileAttach']" placeholder="请输入附件"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="合同文件" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['fileContract']" placeholder="请输入合同文件"  ></a-input>
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
  import JDate from '@/components/jeecg/JDate'  

  export default {
    name: 'ContractGeneralForm',
    components: {
      JFormContainer,
      JDate,
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
          add: "/contract/contractGeneral/add",
          edit: "/contract/contractGeneral/edit",
          queryById: "/contract/contractGeneral/queryById"
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
          this.form.setFieldsValue(pick(this.model,'relateProject','project','isSub','parentId','budget','costCenter','isSecret','startTime','endTime','purchaseType','biddingId','protocol','purchaseId','amount','amountLarge','modelId','filePdf','fileAttach','fileContract'))
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
        this.form.setFieldsValue(pick(row,'relateProject','project','isSub','parentId','budget','costCenter','isSecret','startTime','endTime','purchaseType','biddingId','protocol','purchaseId','amount','amountLarge','modelId','filePdf','fileAttach','fileContract'))
      },
    }
  }
</script>