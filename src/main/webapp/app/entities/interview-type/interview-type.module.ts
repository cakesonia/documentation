import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Lab3SharedModule } from 'app/shared/shared.module';
import { InterviewTypeComponent } from './interview-type.component';
import { InterviewTypeDetailComponent } from './interview-type-detail.component';
import { InterviewTypeUpdateComponent } from './interview-type-update.component';
import { InterviewTypeDeleteDialogComponent } from './interview-type-delete-dialog.component';
import { interviewTypeRoute } from './interview-type.route';

@NgModule({
  imports: [Lab3SharedModule, RouterModule.forChild(interviewTypeRoute)],
  declarations: [InterviewTypeComponent, InterviewTypeDetailComponent, InterviewTypeUpdateComponent, InterviewTypeDeleteDialogComponent],
  entryComponents: [InterviewTypeDeleteDialogComponent]
})
export class Lab3InterviewTypeModule {}
