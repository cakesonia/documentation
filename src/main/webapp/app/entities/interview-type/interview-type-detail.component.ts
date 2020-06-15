import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInterviewType } from 'app/shared/model/interview-type.model';

@Component({
  selector: 'jhi-interview-type-detail',
  templateUrl: './interview-type-detail.component.html'
})
export class InterviewTypeDetailComponent implements OnInit {
  interviewType: IInterviewType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interviewType }) => (this.interviewType = interviewType));
  }

  previousState(): void {
    window.history.back();
  }
}
