import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Lab3SharedModule } from 'app/shared/shared.module';
import { VacancyComponent } from './vacancy.component';
import { VacancyDetailComponent } from './vacancy-detail.component';
import { VacancyUpdateComponent } from './vacancy-update.component';
import { VacancyDeleteDialogComponent } from './vacancy-delete-dialog.component';
import { vacancyRoute } from './vacancy.route';

@NgModule({
  imports: [Lab3SharedModule, RouterModule.forChild(vacancyRoute)],
  declarations: [VacancyComponent, VacancyDetailComponent, VacancyUpdateComponent, VacancyDeleteDialogComponent],
  entryComponents: [VacancyDeleteDialogComponent]
})
export class Lab3VacancyModule {}
