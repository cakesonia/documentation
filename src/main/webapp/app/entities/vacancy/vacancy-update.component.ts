import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVacancy, Vacancy } from 'app/shared/model/vacancy.model';
import { VacancyService } from './vacancy.service';

@Component({
  selector: 'jhi-vacancy-update',
  templateUrl: './vacancy-update.component.html'
})
export class VacancyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(100)]],
    description: [null, [Validators.maxLength(1000)]],
    salary: [],
    createdDate: []
  });

  constructor(protected vacancyService: VacancyService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vacancy }) => {
      if (!vacancy.id) {
        const today = moment().startOf('day');
        vacancy.createdDate = today;
      }

      this.updateForm(vacancy);
    });
  }

  updateForm(vacancy: IVacancy): void {
    this.editForm.patchValue({
      id: vacancy.id,
      title: vacancy.title,
      description: vacancy.description,
      salary: vacancy.salary,
      createdDate: vacancy.createdDate ? vacancy.createdDate.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vacancy = this.createFromForm();
    if (vacancy.id !== undefined) {
      this.subscribeToSaveResponse(this.vacancyService.update(vacancy));
    } else {
      this.subscribeToSaveResponse(this.vacancyService.create(vacancy));
    }
  }

  private createFromForm(): IVacancy {
    return {
      ...new Vacancy(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      salary: this.editForm.get(['salary'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVacancy>>): void {
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
