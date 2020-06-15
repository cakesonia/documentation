import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInterviewResult } from 'app/shared/model/interview-result.model';
import { InterviewResultService } from './interview-result.service';

@Component({
  templateUrl: './interview-result-delete-dialog.component.html'
})
export class InterviewResultDeleteDialogComponent {
  interviewResult?: IInterviewResult;

  constructor(
    protected interviewResultService: InterviewResultService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interviewResultService.delete(id).subscribe(() => {
      this.eventManager.broadcast('interviewResultListModification');
      this.activeModal.close();
    });
  }
}
