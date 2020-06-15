import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Lab3SharedModule } from 'app/shared/shared.module';
import { CandidateComponent } from './candidate.component';
import { CandidateDetailComponent } from './candidate-detail.component';
import { CandidateUpdateComponent } from './candidate-update.component';
import { CandidateDeleteDialogComponent } from './candidate-delete-dialog.component';
import { candidateRoute } from './candidate.route';

@NgModule({
  imports: [Lab3SharedModule, RouterModule.forChild(candidateRoute)],
  declarations: [CandidateComponent, CandidateDetailComponent, CandidateUpdateComponent, CandidateDeleteDialogComponent],
  entryComponents: [CandidateDeleteDialogComponent]
})
export class Lab3CandidateModule {}
