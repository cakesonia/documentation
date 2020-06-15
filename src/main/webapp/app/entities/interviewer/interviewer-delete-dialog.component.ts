import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInterviewer } from 'app/shared/model/interviewer.model';
import { InterviewerService } from './interviewer.service';

@Component({
  templateUrl: './interviewer-delete-dialog.component.html'
})
export class InterviewerDeleteDialogComponent {
  interviewer?: IInterviewer;

  constructor(
    protected interviewerService: InterviewerService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interviewerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('interviewerListModification');
      this.activeModal.close();
    });
  }
}
