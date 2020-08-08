
<#if components?has_content>
    <#list components as component>
<${component}></${component}>
    </#list>
</#if>
<router-outlet></router-outlet>

