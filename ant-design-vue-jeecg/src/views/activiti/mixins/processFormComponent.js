export default [
  {
    text: '一般合同表单',
    routeName: '@/views/contract/manage/modules/ContractGeneralForm',
    component: () => import(`@/views/contract/manage/modules/ContractGeneralForm`),
    businessTable: 'contract_purchase'
  },
  {
    text: '示例表单',
    routeName: '@/views/activiti/form/demoForm',
    component: () => import(`@/views/activiti/form/demoForm`),
    businessTable: 'test_demo'
  },
]