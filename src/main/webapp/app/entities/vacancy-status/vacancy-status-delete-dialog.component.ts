import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVacancyStatus } from 'app/shared/model/vacancy-status.model';
import { VacancyStatusService } from './vacancy-status.service';

@Component({
  templateUrl: './vacancy-status-delete-dialog.component.html'
})
export class VacancyStatusDeleteDialogComponent {
  vacancyStatus?: IVacancyStatus;

  constructor(
    protected vacancyStatusService: VacancyStatusService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vacancyStatusService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vacancyStatusListModification');
      this.activeModal.close();
    });
  }
}
