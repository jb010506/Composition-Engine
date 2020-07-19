import {NgModule} from "@angular/core";
import {Routes, RouterModule } from '@angular/router';
import {ConfigContext} from "./app.config";

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'main',
    pathMatch: 'full',
  },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes.concat(
    ConfigContext.getInstance().routing
  )) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
