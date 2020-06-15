import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterviewResult } from 'app/shared/model/interview-result.model';
import { InterviewResultService } from './interview-result.service';
import { InterviewResultDeleteDialogComponent } from './interview-result-delete-dialog.component';

@Component({
  selector: 'jhi-interview-result',
  templateUrl: './interview-result.component.html'
})
export class InterviewResultComponent implements OnInit, OnDestroy {
  interviewResults?: IInterviewResult[];
  eventSubscriber?: Subscription;

  constructor(
    protected interviewResultService: InterviewResultService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.interviewResultService.query().subscribe((res: HttpResponse<IInterviewResult[]>) => (this.interviewResults = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInterviewResults();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInterviewResult): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInterviewResults(): void {
    this.eventSubscriber = this.eventManager.subscribe('interviewResultListModification', () => this.loadAll());
  }

  delete(interviewResult: IInterviewResult): void {
    const modalRef = this.modalService.open(InterviewResultDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.interviewResult = interviewResult;
  }
}
