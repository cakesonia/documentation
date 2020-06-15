import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IVacancyStatus, VacancyStatus } from 'app/shared/model/vacancy-status.model';
import { VacancyStatusService } from './vacancy-status.service';
import { IVacancy } from 'app/shared/model/vacancy.model';
import { VacancyService } from 'app/entities/vacancy/vacancy.service';

@Component({
  selector: 'jhi-vacancy-status-update',
  templateUrl: './vacancy-status-update.component.html'
})
export class VacancyStatusUpdateComponent implements OnInit {
  isSaving = false;
  vacancies: IVacancy[] = [];

  editForm = this.fb.group({
    id: [],
    status: [null, [Validators.required, Validators.maxLength(100)]],
    vacancy: []
  });

  constructor(
    protected vacancyStatusService: VacancyStatusService,
    protected vacancyService: VacancyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vacancyStatus }) => {
      this.updateForm(vacancyStatus);

      this.vacancyService.query().subscribe((res: HttpResponse<IVacancy[]>) => (this.vacancies = res.body || []));
    });
  }

  updateForm(vacancyStatus: IVacancyStatus): void {
    this.editForm.patchValue({
      id: vacancyStatus.id,
      status: vacancyStatus.status,
      vacancy: vacancyStatus.vacancy
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vacancyStatus = this.createFromForm();
    if (vacancyStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.vacancyStatusService.update(vacancyStatus));
    } else {
      this.subscribeToSaveResponse(this.vacancyStatusService.create(vacancyStatus));
    }
  }

  private createFromForm(): IVacancyStatus {
    return {
      ...new VacancyStatus(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      vacancy: this.editForm.get(['vacancy'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVacancyStatus>>): void {
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

  trackById(index: number, item: IVacancy): any {
    return item.id;
  }
}
