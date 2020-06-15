import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterviewer } from 'app/shared/model/interviewer.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { InterviewerService } from './interviewer.service';
import { InterviewerDeleteDialogComponent } from './interviewer-delete-dialog.component';

@Component({
  selector: 'jhi-interviewer',
  templateUrl: './interviewer.component.html'
})
export class InterviewerComponent implements OnInit, OnDestroy {
  interviewers: IInterviewer[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected interviewerService: InterviewerService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.interviewers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.interviewerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IInterviewer[]>) => this.paginateInterviewers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.interviewers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInterviewers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInterviewer): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInterviewers(): void {
    this.eventSubscriber = this.eventManager.subscribe('interviewerListModification', () => this.reset());
  }

  delete(interviewer: IInterviewer): void {
    const modalRef = this.modalService.open(InterviewerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.interviewer = interviewer;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInterviewers(data: IInterviewer[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.interviewers.push(data[i]);
      }
    }
  }
}
