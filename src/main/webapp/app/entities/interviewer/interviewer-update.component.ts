import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInterviewer, Interviewer } from 'app/shared/model/interviewer.model';
import { InterviewerService } from './interviewer.service';

@Component({
  selector: 'jhi-interviewer-update',
  templateUrl: './interviewer-update.component.html'
})
export class InterviewerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fullName: [null, [Validators.required, Validators.maxLength(100)]],
    email: [null, [Validators.maxLength(100)]],
    position: [null, [Validators.maxLength(100)]]
  });

  constructor(protected interviewerService: InterviewerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interviewer }) => {
      this.updateForm(interviewer);
    });
  }

  updateForm(interviewer: IInterviewer): void {
    this.editForm.patchValue({
      id: interviewer.id,
      fullName: interviewer.fullName,
      email: interviewer.email,
      position: interviewer.position
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interviewer = this.createFromForm();
    if (interviewer.id !== undefined) {
      this.subscribeToSaveResponse(this.interviewerService.update(interviewer));
    } else {
      this.subscribeToSaveResponse(this.interviewerService.create(interviewer));
    }
  }

  private createFromForm(): IInterviewer {
    return {
      ...new Interviewer(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      email: this.editForm.get(['email'])!.value,
      position: this.editForm.get(['position'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterviewer>>): void {
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
