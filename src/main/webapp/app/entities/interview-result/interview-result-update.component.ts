import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInterviewResult, InterviewResult } from 'app/shared/model/interview-result.model';
import { InterviewResultService } from './interview-result.service';

@Component({
  selector: 'jhi-interview-result-update',
  templateUrl: './interview-result-update.component.html'
})
export class InterviewResultUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.maxLength(1000)]]
  });

  constructor(
    protected interviewResultService: InterviewResultService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interviewResult }) => {
      this.updateForm(interviewResult);
    });
  }

  updateForm(interviewResult: IInterviewResult): void {
    this.editForm.patchValue({
      id: interviewResult.id,
      description: interviewResult.description
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interviewResult = this.createFromForm();
    if (interviewResult.id !== undefined) {
      this.subscribeToSaveResponse(this.interviewResultService.update(interviewResult));
    } else {
      this.subscribeToSaveResponse(this.interviewResultService.create(interviewResult));
    }
  }

  private createFromForm(): IInterviewResult {
    return {
      ...new InterviewResult(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterviewResult>>): void {
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
}
