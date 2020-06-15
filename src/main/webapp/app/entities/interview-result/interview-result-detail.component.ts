import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInterviewResult } from 'app/shared/model/interview-result.model';

@Component({
  selector: 'jhi-interview-result-detail',
  templateUrl: './interview-result-detail.component.html'
})
export class InterviewResultDetailComponent implements OnInit {
  interviewResult: IInterviewResult | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interviewResult }) => (this.interviewResult = interviewResult));
  }

  previousState(): void {
    window.history.back();
  }
}
