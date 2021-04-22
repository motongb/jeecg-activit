export default [
  {
    text: '一般合同表单',
    routeName: 'ContractGeneralForm',
    component: () => import(`@/views/contract/manage/modules/ContractGeneralForm`),
    businessTable: 'contract_purchase'
  },
  {
    text: '用印申请表单',
    routeName: 'ContractStampForm',
    component: () => import(`@/views/contract/manage/modules/ContractStampForm`),
    businessTable: 'contract_stamp'
  },
  {
    text: '示例表单',
    routeName: 'demoForm',
    component: () => import(`@/views/activiti/form/demoForm`),
    businessTable: 'test_demo'
  },
]