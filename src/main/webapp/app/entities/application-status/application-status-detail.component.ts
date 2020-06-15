import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApplicationStatus } from 'app/shared/model/application-status.model';

@Component({
  selector: 'jhi-application-status-detail',
  templateUrl: './application-status-detail.component.html'
})
export class ApplicationStatusDetailComponent implements OnInit {
  applicationStatus: IApplicationStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationStatus }) => (this.applicationStatus = applicationStatus));
  }

  previousState(): void {
    window.history.back();
  }
}
