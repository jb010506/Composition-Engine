import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { PERFECT_SCROLLBAR_CONFIG } from 'ngx-perfect-scrollbar';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
suppressScrollX: true
};

import { AppComponent } from './app.component';

// Import containers
import { DefaultLayoutComponent } from './containers';
<#if pages?has_content>
    <#list pages as page_capitalized, page>
import {${page_capitalized}Component}from "./${page}/${page}.component";
    </#list>
</#if>

<#if components?has_content>
    <#list selectorPageMap as component, pageSelector>
import { ${component?capitalize}Component} from './${pageSelector}/${component}/${component}.component';
    </#list>
</#if>


const APP_CONTAINERS = [
DefaultLayoutComponent
];

import {
AppAsideModule,
AppBreadcrumbModule,
AppHeaderModule,
AppFooterModule,
AppSidebarModule,
} from '@coreui/angular';

// Import routing module
import { AppRoutingModule } from './app.routing';

// Import 3rd party components
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ChartsModule } from 'ng2-charts';

@NgModule({
imports: [
BrowserModule,
BrowserAnimationsModule,
AppRoutingModule,
AppAsideModule,
AppBreadcrumbModule.forRoot(),
AppFooterModule,
AppHeaderModule,
AppSidebarModule,
PerfectScrollbarModule,
BsDropdownModule.forRoot(),
TabsModule.forRoot(),
ChartsModule
],
declarations: [
<#if pages?has_content>
    <#list pages as page_capitalized, page>
${page_capitalized}Component,
    </#list>
</#if>

<#if components?has_content>
    <#list components as component_capitalized, component>
${component_capitalized}Component,
    </#list>
</#if>
AppComponent,
...APP_CONTAINERS,

],
providers: [{
provide: LocationStrategy,
useClass: HashLocationStrategy
}],
bootstrap: [ AppComponent ]
})
export class AppModule { }
