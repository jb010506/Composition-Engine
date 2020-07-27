import {Component} from '@angular/core';
import {INavData} from "@coreui/angular";

@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html'
})
export class DefaultLayoutComponent {
  public sidebarMinimized = false;
    navItems: INavData[] = [${items}]


  toggleMinimize(e) {
    this.sidebarMinimized = e;
  }

}
