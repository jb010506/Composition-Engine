import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Import Containers
import { DefaultLayoutComponent } from './containers';
import {Page1Component} from "./page1/page1.component";
import {Page3Component} from "./page3/page3.component";
import {Page2Component} from "./page2/page2.component";


export const routes: Routes = [
  {
    path: '',
    redirectTo:'index',
    pathMatch: 'full'
  },
  {
    path: 'index',
    component: DefaultLayoutComponent,
    children:[{
      path: 'page1',
      component:Page1Component,
      children:[{
        path: 'selab',
        component:Page3Component,
      }
      ]
    },
      {
        path: 'news',
        component:Page2Component,
        children:[

        ]
      }
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
