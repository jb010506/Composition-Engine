<app-header
        [navbarBrandRouterLink]="['/dashboard']"
        [fixed]="true"
        [navbarBrandFull]="{src: 'assets/img/brand/logo1.svg', width: 89, height: 25, alt: 'SELab'}"
        [navbarBrandMinimized]="{src: 'assets/img/brand/sygnet.svg', width: 30, height: 30, alt: 'CoreUI Logo'}"
        [sidebarToggler]="'lg'"
        [asideMenuToggler]="'lg'">

    <ul class="nav navbar-nav d-md-down-none">
        <#list headerItems as headerItem>
            <li class="nav-item px-3">
                <a class="nav-link" href="#">${headerItem.text}</a>
            </li>
        </#list>
    </ul>
    <ul class="nav navbar-nav ml-auto">
        <li class="nav-item d-md-down-none">
<#--            <a class="nav-link" href="#"><i class="icon-bell"></i><span class="badge badge-pill badge-danger">5</span></a>-->
        </li>
<#--        <!--    <li class="nav-item d-md-down-none">&ndash;&gt;-->
<#--        <!--      <a class="nav-link" href="#"><i class="icon-list"></i></a>&ndash;&gt;-->
<#--        <!--    </li>&ndash;&gt;-->
<#--        <!--    <li class="nav-item d-md-down-none">&ndash;&gt;-->
<#--        <!--      <a class="nav-link" href="#"><i class="icon-location-pin"></i></a>&ndash;&gt;-->
<#--        <!--    </li>&ndash;&gt;-->
    </ul>
</app-header>
<div class="app-body">
    <app-sidebar #appSidebar [fixed]="true" [display]="'lg'" [minimized]="sidebarMinimized" (minimizedChange)="toggleMinimize($event)">
        <app-sidebar-nav [perfectScrollbar] [disabled]="appSidebar.minimized" [navItems]="navItems"></app-sidebar-nav>
        <app-sidebar-minimizer></app-sidebar-minimizer>
    </app-sidebar>
    <!-- Main content -->
    <main class="main" style="background: white">

        <div class="container-fluid">
            <page1></page1>
        </div>
    </main>
    <app-aside [fixed]="true" [display]="'lg'">
        <tabset>
            <#list asidebarItems as asidebarItem>
                <span><a class="btn btn-light" href="${asidebarItem.href}">${asidebarItem.text}</a></span>
            </#list>
        </tabset>
    </app-aside>
</div>
<app-footer>
    <#list footerItems as footerItem>
        <#if footerItem.href!="">
            <span><a href="${footerItem.href}">${footerItem.text}</a></span>
        <#else>
            <span><a>${footerItem.text}</a></span>
        </#if>
    </#list>

<#--    <span class="ml-auto">Powered by <a href="https://coreui.io/angular">CoreUI for Angular</a></span>-->
</app-footer>

