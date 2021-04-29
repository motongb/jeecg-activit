
- DepartListSync.vue 部门管理列表，机构类型修改

```
<a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="机构类型">
<!--                <template v-if="orgCategoryDisabled">-->
<!--                  <a-radio-group v-decorator="['orgCategory',validatorRules.orgCategory]" placeholder="请选择机构类型">-->
<!--                    <a-radio value="1">-->
<!--                      公司-->
<!--                    </a-radio>-->
<!--                  </a-radio-group>-->
<!--                </template>-->
<!--                <template v-else>-->
<!--                  <a-radio-group v-decorator="['orgCategory',validatorRules.orgCategory]" placeholder="请选择机构类型">-->
<!--                    <a-radio value="2">-->
<!--                      部门-->
<!--                    </a-radio>-->
<!--                    <a-radio value="3">-->
<!--                      岗位-->
<!--                    </a-radio>-->
<!--                  </a-radio-group>-->
<!--                </template>-->
                <j-dict-select-tag type="radio" v-decorator="['orgCategory',validatorRules.orgCategory]" dictCode="org_category"/>
              </a-form-item>
```

- JSelectDepartModal.vue 机构选择模态框修改

```
initDepartComponent(){
        let names = ''
        if(this.departId){
          let currDepartId = this.departId
          for(let item of this.dataList){
            if(currDepartId===item.key){
              names+=","+item.title
            }
          }
          if(names){
            names = names.substring(1)
          }
        }
        this.$emit("initComp",names)
      },
```

- JModal强制渲染

```
src/components/jeecg/JModal/index.vue
forceRender: Boolean,
```