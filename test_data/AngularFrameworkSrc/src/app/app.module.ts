import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {AppRoutingModule} from "./app.routing.module";

// Import 3rd party components
import {CommonModule, HashLocationStrategy, LocationStrategy} from "@angular/common";

// Import Selab UI
import {FormsModule} from "@angular/forms";
import {BrowserAnimationsModule, NoopAnimationsModule} from "@angular/platform-browser/animations";

import {ConfigContext} from './app.config'


@NgModule({
  declarations:
    [
      AppComponent,
    ]
      .concat(
      ConfigContext.getInstance().declarationComponent
  ),
  imports:
    [CommonModule,
      FormsModule,
      BrowserAnimationsModule,
      AppRoutingModule
    ]
      .concat(
      ConfigContext.getInstance().moduleImport
    ),
  entryComponents:
    []
      .concat(
      ConfigContext.getInstance().entryComponent
    ),
  providers:
    [
      {
        provide: LocationStrategy,
        useClass: HashLocationStrategy
      },
    ]
      .concat(
      ConfigContext.getInstance().providers
    ),
  bootstrap: [AppComponent],
})
export class AppModule { }
