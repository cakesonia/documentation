import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVacancyStatus } from 'app/shared/model/vacancy-status.model';

@Component({
  selector: 'jhi-vacancy-status-detail',
  templateUrl: './vacancy-status-detail.component.html'
})
export class VacancyStatusDetailComponent implements OnInit {
  vacancyStatus: IVacancyStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vacancyStatus }) => (this.vacancyStatus = vacancyStatus));
  }

  previousState(): void {
    window.history.back();
  }
}
