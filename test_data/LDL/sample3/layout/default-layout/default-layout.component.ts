import {
  Component,
  OnDestroy,
  Inject,
  ComponentFactoryResolver,
  Injector
} from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { NavData, navItems, newNav } from './_nav';
import {DefaultLayoutService} from "./default-layout.service";
import {Subscription} from "rxjs";


@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html',
  styleUrls:['./default-layout.component.scss']
})
export class DefaultLayoutComponent implements OnDestroy {
  public navItems : NavData[] = [{
    name: 'Dashboard',
    url: '/dashboard',
    icon: 'icon-speedometer',
  }];
  public sidebarMinimized = true;
  private changes: MutationObserver;
  public element: HTMLElement;

  navSubscription: Subscription;

  constructor(private service: DefaultLayoutService,
              private componentFactoryResolver: ComponentFactoryResolver,
              private injector: Injector,
              @Inject(DOCUMENT) _document?: any,
              ) {
    this.changes = new MutationObserver((mutations) => {
      this.sidebarMinimized = _document.body.classList.contains('sidebar-minimized');
    });
    this.element = _document.body;
    this.changes.observe(<Element>this.element, {
      attributes: true,
      attributeFilter: ['class']
    });

  }

  ngAfterViewInit(): void{
    this.navSubscription = this.service.navigationOperationStatus.subscribe((data) => this.navFunction(data) );
  }

  ngOnDestroy(): void {
    this.navSubscription.unsubscribe();
    this.changes.disconnect();
  }

  navFunction(data: {}){
    this.navItems = [];
    for (const [op, status] of Object.entries(data)) {
      if (status["notification"] === "READY") {
        this.navItems.push({
          name: op,
          url: '/' + op,
          icon: 'icon-mouse'
        });
      }
    }
  }


}
