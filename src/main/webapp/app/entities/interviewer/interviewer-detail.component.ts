import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInterviewer } from 'app/shared/model/interviewer.model';

@Component({
  selector: 'jhi-interviewer-detail',
  templateUrl: './interviewer-detail.component.html'
})
export class InterviewerDetailComponent implements OnInit {
  interviewer: IInterviewer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interviewer }) => (this.interviewer = interviewer));
  }

  previousState(): void {
    window.history.back();
  }
}
