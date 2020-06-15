import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVacancyStatus } from 'app/shared/model/vacancy-status.model';
import { VacancyStatusService } from './vacancy-status.service';
import { VacancyStatusDeleteDialogComponent } from './vacancy-status-delete-dialog.component';

@Component({
  selector: 'jhi-vacancy-status',
  templateUrl: './vacancy-status.component.html'
})
export class VacancyStatusComponent implements OnInit, OnDestroy {
  vacancyStatuses?: IVacancyStatus[];
  eventSubscriber?: Subscription;

  constructor(
    protected vacancyStatusService: VacancyStatusService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.vacancyStatusService.query().subscribe((res: HttpResponse<IVacancyStatus[]>) => (this.vacancyStatuses = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVacancyStatuses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVacancyStatus): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVacancyStatuses(): void {
    this.eventSubscriber = this.eventManager.subscribe('vacancyStatusListModification', () => this.loadAll());
  }

  delete(vacancyStatus: IVacancyStatus): void {
    const modalRef = this.modalService.open(VacancyStatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vacancyStatus = vacancyStatus;
  }
}
