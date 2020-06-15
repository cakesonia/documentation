import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Lab3SharedModule } from 'app/shared/shared.module';
import { InterviewerComponent } from './interviewer.component';
import { InterviewerDetailComponent } from './interviewer-detail.component';
import { InterviewerUpdateComponent } from './interviewer-update.component';
import { InterviewerDeleteDialogComponent } from './interviewer-delete-dialog.component';
import { interviewerRoute } from './interviewer.route';

@NgModule({
  imports: [Lab3SharedModule, RouterModule.forChild(interviewerRoute)],
  declarations: [InterviewerComponent, InterviewerDetailComponent, InterviewerUpdateComponent, InterviewerDeleteDialogComponent],
  entryComponents: [InterviewerDeleteDialogComponent]
})
export class Lab3InterviewerModule {}
