import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IApplication, Application } from 'app/shared/model/application.model';
import { ApplicationService } from './application.service';
import { IVacancy } from 'app/shared/model/vacancy.model';
import { VacancyService } from 'app/entities/vacancy/vacancy.service';

@Component({
  selector: 'jhi-application-update',
  templateUrl: './application-update.component.html'
})
export class ApplicationUpdateComponent implements OnInit {
  isSaving = false;
  vacancies: IVacancy[] = [];

  editForm = this.fb.group({
    id: [],
    applicationDate: [],
    vacancy: []
  });

  constructor(
    protected applicationService: ApplicationService,
    protected vacancyService: VacancyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ application }) => {
      if (!application.id) {
        const today = moment().startOf('day');
        application.applicationDate = today;
      }

      this.updateForm(application);

      this.vacancyService.query().subscribe((res: HttpResponse<IVacancy[]>) => (this.vacancies = res.body || []));
    });
  }

  updateForm(application: IApplication): void {
    this.editForm.patchValue({
      id: application.id,
      applicationDate: application.applicationDate ? application.applicationDate.format(DATE_TIME_FORMAT) : null,
      vacancy: application.vacancy
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const application = this.createFromForm();
    if (application.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationService.update(application));
    } else {
      this.subscribeToSaveResponse(this.applicationService.create(application));
    }
  }

  private createFromForm(): IApplication {
    return {
      ...new Application(),
      id: this.editForm.get(['id'])!.value,
      applicationDate: this.editForm.get(['applicationDate'])!.value
        ? moment(this.editForm.get(['applicationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      vacancy: this.editForm.get(['vacancy'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplication>>): void {
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
