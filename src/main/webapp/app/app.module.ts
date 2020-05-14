import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { Lab3SharedModule } from 'app/shared/shared.module';
import { Lab3CoreModule } from 'app/core/core.module';
import { Lab3AppRoutingModule } from './app-routing.module';
import { Lab3HomeModule } from './home/home.module';
import { Lab3EntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    Lab3SharedModule,
    Lab3CoreModule,
    Lab3HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    Lab3EntityModule,
    Lab3AppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class Lab3AppModule {}
