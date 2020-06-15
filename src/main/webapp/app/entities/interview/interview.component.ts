import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInterview } from 'app/shared/model/interview.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { InterviewService } from './interview.service';
import { InterviewDeleteDialogComponent } from './interview-delete-dialog.component';

@Component({
  selector: 'jhi-interview',
  templateUrl: './interview.component.html'
})
export class InterviewComponent implements OnInit, OnDestroy {
  interviews: IInterview[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected interviewService: InterviewService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.interviews = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.interviewService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IInterview[]>) => this.paginateInterviews(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.interviews = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInterviews();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInterview): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInterviews(): void {
    this.eventSubscriber = this.eventManager.subscribe('interviewListModification', () => this.reset());
  }

  delete(interview: IInterview): void {
    const modalRef = this.modalService.open(InterviewDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.interview = interview;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInterviews(data: IInterview[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.interviews.push(data[i]);
      }
    }
  }
}
