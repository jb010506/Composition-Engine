<app-header
        [navbarBrandRouterLink]="['/dashboard']"
        [fixed]="true"
        [navbarBrandFull]="{src: 'assets/img/brand/logo.svg', width: 89, height: 25, alt: 'CoreUI Logo'}"
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
            <a class="nav-link" href="#"><i class="icon-bell"></i><span class="badge badge-pill badge-danger">5</span></a>
        </li>
        <!--    <li class="nav-item d-md-down-none">-->
        <!--      <a class="nav-link" href="#"><i class="icon-list"></i></a>-->
        <!--    </li>-->
        <!--    <li class="nav-item d-md-down-none">-->
        <!--      <a class="nav-link" href="#"><i class="icon-location-pin"></i></a>-->
        <!--    </li>-->
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
    <app-aside [fixed]="true" [display]="false">
        <tabset>
        </tabset>
    </app-aside>
</div>
<app-footer>
    <span><a href="https://coreui.io">CoreUI</a> &copy; 2020 SELabs.</span>
<#--    <span class="ml-auto">Powered by <a href="https://coreui.io/angular">CoreUI for Angular</a></span>-->
</app-footer>

