import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IInterview, Interview } from 'app/shared/model/interview.model';
import { InterviewService } from './interview.service';
import { IInterviewResult } from 'app/shared/model/interview-result.model';
import { InterviewResultService } from 'app/entities/interview-result/interview-result.service';
import { ICandidate } from 'app/shared/model/candidate.model';
import { CandidateService } from 'app/entities/candidate/candidate.service';
import { IInterviewer } from 'app/shared/model/interviewer.model';
import { InterviewerService } from 'app/entities/interviewer/interviewer.service';

type SelectableEntity = IInterviewResult | ICandidate | IInterviewer;

@Component({
  selector: 'jhi-interview-update',
  templateUrl: './interview-update.component.html'
})
export class InterviewUpdateComponent implements OnInit {
  isSaving = false;
  results: IInterviewResult[] = [];
  candidates: ICandidate[] = [];
  interviewers: IInterviewer[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    result: [],
    candidate: [],
    interviewer: []
  });

  constructor(
    protected interviewService: InterviewService,
    protected interviewResultService: InterviewResultService,
    protected candidateService: CandidateService,
    protected interviewerService: InterviewerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interview }) => {
      if (!interview.id) {
        const today = moment().startOf('day');
        interview.date = today;
      }

      this.updateForm(interview);

      this.interviewResultService
        .query({ filter: 'interview-is-null' })
        .pipe(
          map((res: HttpResponse<IInterviewResult[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IInterviewResult[]) => {
          if (!interview.result || !interview.result.id) {
            this.results = resBody;
          } else {
            this.interviewResultService
              .find(interview.result.id)
              .pipe(
                map((subRes: HttpResponse<IInterviewResult>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IInterviewResult[]) => (this.results = concatRes));
          }
        });

      this.candidateService.query().subscribe((res: HttpResponse<ICandidate[]>) => (this.candidates = res.body || []));

      this.interviewerService.query().subscribe((res: HttpResponse<IInterviewer[]>) => (this.interviewers = res.body || []));
    });
  }

  updateForm(interview: IInterview): void {
    this.editForm.patchValue({
      id: interview.id,
      date: interview.date ? interview.date.format(DATE_TIME_FORMAT) : null,
      result: interview.result,
      candidate: interview.candidate,
      interviewer: interview.interviewer
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interview = this.createFromForm();
    if (interview.id !== undefined) {
      this.subscribeToSaveResponse(this.interviewService.update(interview));
    } else {
      this.subscribeToSaveResponse(this.interviewService.create(interview));
    }
  }

  private createFromForm(): IInterview {
    return {
      ...new Interview(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      result: this.editForm.get(['result'])!.value,
      candidate: this.editForm.get(['candidate'])!.value,
      interviewer: this.editForm.get(['interviewer'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterview>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
