import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IApplicationStatus } from 'app/shared/model/application-status.model';
import { ApplicationStatusService } from './application-status.service';

@Component({
  templateUrl: './application-status-delete-dialog.component.html'
})
export class ApplicationStatusDeleteDialogComponent {
  applicationStatus?: IApplicationStatus;

  constructor(
    protected applicationStatusService: ApplicationStatusService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.applicationStatusService.delete(id).subscribe(() => {
      this.eventManager.broadcast('applicationStatusListModification');
      this.activeModal.close();
    });
  }
}
