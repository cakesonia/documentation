import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVacancy } from 'app/shared/model/vacancy.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VacancyService } from './vacancy.service';
import { VacancyDeleteDialogComponent } from './vacancy-delete-dialog.component';

@Component({
  selector: 'jhi-vacancy',
  templateUrl: './vacancy.component.html'
})
export class VacancyComponent implements OnInit, OnDestroy {
  vacancies: IVacancy[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected vacancyService: VacancyService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.vacancies = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.vacancyService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IVacancy[]>) => this.paginateVacancies(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.vacancies = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVacancies();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVacancy): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVacancies(): void {
    this.eventSubscriber = this.eventManager.subscribe('vacancyListModification', () => this.reset());
  }

  delete(vacancy: IVacancy): void {
    const modalRef = this.modalService.open(VacancyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vacancy = vacancy;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVacancies(data: IVacancy[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.vacancies.push(data[i]);
      }
    }
  }
}
