import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInterview } from 'app/shared/model/interview.model';
import { InterviewService } from './interview.service';

@Component({
  templateUrl: './interview-delete-dialog.component.html'
})
export class InterviewDeleteDialogComponent {
  interview?: IInterview;

  constructor(protected interviewService: InterviewService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interviewService.delete(id).subscribe(() => {
      this.eventManager.broadcast('interviewListModification');
      this.activeModal.close();
    });
  }
}
