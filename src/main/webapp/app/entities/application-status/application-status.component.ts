import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplicationStatus } from 'app/shared/model/application-status.model';
import { ApplicationStatusService } from './application-status.service';
import { ApplicationStatusDeleteDialogComponent } from './application-status-delete-dialog.component';

@Component({
  selector: 'jhi-application-status',
  templateUrl: './application-status.component.html'
})
export class ApplicationStatusComponent implements OnInit, OnDestroy {
  applicationStatuses?: IApplicationStatus[];
  eventSubscriber?: Subscription;

  constructor(
    protected applicationStatusService: ApplicationStatusService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.applicationStatusService
      .query()
      .subscribe((res: HttpResponse<IApplicationStatus[]>) => (this.applicationStatuses = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInApplicationStatuses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IApplicationStatus): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInApplicationStatuses(): void {
    this.eventSubscriber = this.eventManager.subscribe('applicationStatusListModification', () => this.loadAll());
  }

  delete(applicationStatus: IApplicationStatus): void {
    const modalRef = this.modalService.open(ApplicationStatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.applicationStatus = applicationStatus;
  }
}
