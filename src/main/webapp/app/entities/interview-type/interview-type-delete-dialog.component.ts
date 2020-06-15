import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInterviewType } from 'app/shared/model/interview-type.model';
import { InterviewTypeService } from './interview-type.service';

@Component({
  templateUrl: './interview-type-delete-dialog.component.html'
})
export class InterviewTypeDeleteDialogComponent {
  interviewType?: IInterviewType;

  constructor(
    protected interviewTypeService: InterviewTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interviewTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('interviewTypeListModification');
      this.activeModal.close();
    });
  }
}
