import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IApplicationStatus, ApplicationStatus } from 'app/shared/model/application-status.model';
import { ApplicationStatusService } from './application-status.service';
import { IApplication } from 'app/shared/model/application.model';
import { ApplicationService } from 'app/entities/application/application.service';

@Component({
  selector: 'jhi-application-status-update',
  templateUrl: './application-status-update.component.html'
})
export class ApplicationStatusUpdateComponent implements OnInit {
  isSaving = false;
  applications: IApplication[] = [];

  editForm = this.fb.group({
    id: [],
    status: [null, [Validators.required, Validators.maxLength(100)]],
    application: []
  });

  constructor(
    protected applicationStatusService: ApplicationStatusService,
    protected applicationService: ApplicationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationStatus }) => {
      this.updateForm(applicationStatus);

      this.applicationService.query().subscribe((res: HttpResponse<IApplication[]>) => (this.applications = res.body || []));
    });
  }

  updateForm(applicationStatus: IApplicationStatus): void {
    this.editForm.patchValue({
      id: applicationStatus.id,
      status: applicationStatus.status,
      application: applicationStatus.application
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationStatus = this.createFromForm();
    if (applicationStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationStatusService.update(applicationStatus));
    } else {
      this.subscribeToSaveResponse(this.applicationStatusService.create(applicationStatus));
    }
  }

  private createFromForm(): IApplicationStatus {
    return {
      ...new ApplicationStatus(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      application: this.editForm.get(['application'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationStatus>>): void {
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

  trackById(index: number, item: IApplication): any {
    return item.id;
  }
}
