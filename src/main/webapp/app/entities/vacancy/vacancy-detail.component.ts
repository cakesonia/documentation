import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVacancy } from 'app/shared/model/vacancy.model';

@Component({
  selector: 'jhi-vacancy-detail',
  templateUrl: './vacancy-detail.component.html'
})
export class VacancyDetailComponent implements OnInit {
  vacancy: IVacancy | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vacancy }) => (this.vacancy = vacancy));
  }

  previousState(): void {
    window.history.back();
  }
}
