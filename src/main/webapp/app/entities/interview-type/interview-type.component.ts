import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterviewType } from 'app/shared/model/interview-type.model';
import { InterviewTypeService } from './interview-type.service';
import { InterviewTypeDeleteDialogComponent } from './interview-type-delete-dialog.component';

@Component({
  selector: 'jhi-interview-type',
  templateUrl: './interview-type.component.html'
})
export class InterviewTypeComponent implements OnInit, OnDestroy {
  interviewTypes?: IInterviewType[];
  eventSubscriber?: Subscription;

  constructor(
    protected interviewTypeService: InterviewTypeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.interviewTypeService.query().subscribe((res: HttpResponse<IInterviewType[]>) => (this.interviewTypes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInterviewTypes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInterviewType): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInterviewTypes(): void {
    this.eventSubscriber = this.eventManager.subscribe('interviewTypeListModification', () => this.loadAll());
  }

  delete(interviewType: IInterviewType): void {
    const modalRef = this.modalService.open(InterviewTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.interviewType = interviewType;
  }
}
