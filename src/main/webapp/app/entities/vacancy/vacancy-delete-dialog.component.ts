import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVacancy } from 'app/shared/model/vacancy.model';
import { VacancyService } from './vacancy.service';

@Component({
  templateUrl: './vacancy-delete-dialog.component.html'
})
export class VacancyDeleteDialogComponent {
  vacancy?: IVacancy;

  constructor(protected vacancyService: VacancyService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vacancyService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vacancyListModification');
      this.activeModal.close();
    });
  }
}
