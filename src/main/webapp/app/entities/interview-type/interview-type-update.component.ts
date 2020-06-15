import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInterviewType, InterviewType } from 'app/shared/model/interview-type.model';
import { InterviewTypeService } from './interview-type.service';
import { IInterview } from 'app/shared/model/interview.model';
import { InterviewService } from 'app/entities/interview/interview.service';

@Component({
  selector: 'jhi-interview-type-update',
  templateUrl: './interview-type-update.component.html'
})
export class InterviewTypeUpdateComponent implements OnInit {
  isSaving = false;
  interviews: IInterview[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required, Validators.maxLength(100)]],
    interview: []
  });

  constructor(
    protected interviewTypeService: InterviewTypeService,
    protected interviewService: InterviewService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interviewType }) => {
      this.updateForm(interviewType);

      this.interviewService.query().subscribe((res: HttpResponse<IInterview[]>) => (this.interviews = res.body || []));
    });
  }

  updateForm(interviewType: IInterviewType): void {
    this.editForm.patchValue({
      id: interviewType.id,
      type: interviewType.type,
      interview: interviewType.interview
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interviewType = this.createFromForm();
    if (interviewType.id !== undefined) {
      this.subscribeToSaveResponse(this.interviewTypeService.update(interviewType));
    } else {
      this.subscribeToSaveResponse(this.interviewTypeService.create(interviewType));
    }
  }

  private createFromForm(): IInterviewType {
    return {
      ...new InterviewType(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      interview: this.editForm.get(['interview'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterviewType>>): void {
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

  trackById(index: number, item: IInterview): any {
    return item.id;
  }
}
