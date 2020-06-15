import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Lab3SharedModule } from 'app/shared/shared.module';
import { VacancyStatusComponent } from './vacancy-status.component';
import { VacancyStatusDetailComponent } from './vacancy-status-detail.component';
import { VacancyStatusUpdateComponent } from './vacancy-status-update.component';
import { VacancyStatusDeleteDialogComponent } from './vacancy-status-delete-dialog.component';
import { vacancyStatusRoute } from './vacancy-status.route';

@NgModule({
  imports: [Lab3SharedModule, RouterModule.forChild(vacancyStatusRoute)],
  declarations: [VacancyStatusComponent, VacancyStatusDetailComponent, VacancyStatusUpdateComponent, VacancyStatusDeleteDialogComponent],
  entryComponents: [VacancyStatusDeleteDialogComponent]
})
export class Lab3VacancyStatusModule {}
