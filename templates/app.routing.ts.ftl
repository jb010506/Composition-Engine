import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Import Containers
import { DefaultLayoutComponent } from './containers';

<#list components as component>
import { ${component}Component } from './${component?lower_case}/${component?lower_case}.component'
</#list>

<#assign root = ndl>
<#macro test content>{
    path: "${content.path}",
    component: ${content.component}Component,
    children:[
    <#local level = content.children>
    <#if level.length()==0>
        ],
        },
        <#return >
    </#if>

    <#list 0..level.length()-1 as x>
        <@test level.get(x)></@test>
    </#list>
    ]
    },
</#macro>



<#--<#macro test level0>-->
<#--    <#recurse level0.children?children as child>-->
<#--    path: "${level0.path}",-->
<#--    component: ${level0.component}Component,-->
<#--    children:[-->
<#--    <#assign level1 = level0.children>-->
<#--    <#list 0..level1.length()-1 as x>-->
<#--            <@test level1></@test>-->
<#--        </#list>-->
<#--    ],-->
<#--</#macro>-->




export const routes: Routes = [
<@test root></@test>
]
@NgModule({
imports: [ RouterModule.forRoot(routes) ],
exports: [ RouterModule ]
})
export class AppRoutingModule {}
