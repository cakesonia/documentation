import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Lab3SharedModule } from 'app/shared/shared.module';
import { ApplicationStatusComponent } from './application-status.component';
import { ApplicationStatusDetailComponent } from './application-status-detail.component';
import { ApplicationStatusUpdateComponent } from './application-status-update.component';
import { ApplicationStatusDeleteDialogComponent } from './application-status-delete-dialog.component';
import { applicationStatusRoute } from './application-status.route';

@NgModule({
  imports: [Lab3SharedModule, RouterModule.forChild(applicationStatusRoute)],
  declarations: [
    ApplicationStatusComponent,
    ApplicationStatusDetailComponent,
    ApplicationStatusUpdateComponent,
    ApplicationStatusDeleteDialogComponent
  ],
  entryComponents: [ApplicationStatusDeleteDialogComponent]
})
export class Lab3ApplicationStatusModule {}
