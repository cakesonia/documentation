import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICandidate, Candidate } from 'app/shared/model/candidate.model';
import { CandidateService } from './candidate.service';
import { IVacancy } from 'app/shared/model/vacancy.model';
import { VacancyService } from 'app/entities/vacancy/vacancy.service';

@Component({
  selector: 'jhi-candidate-update',
  templateUrl: './candidate-update.component.html'
})
export class CandidateUpdateComponent implements OnInit {
  isSaving = false;
  vacancies: IVacancy[] = [];

  editForm = this.fb.group({
    id: [],
    fullName: [null, [Validators.required, Validators.maxLength(100)]],
    email: [null, [Validators.required, Validators.maxLength(100)]],
    cvUrl: [null, [Validators.maxLength(100)]],
    vacancies: []
  });

  constructor(
    protected candidateService: CandidateService,
    protected vacancyService: VacancyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidate }) => {
      this.updateForm(candidate);

      this.vacancyService.query().subscribe((res: HttpResponse<IVacancy[]>) => (this.vacancies = res.body || []));
    });
  }

  updateForm(candidate: ICandidate): void {
    this.editForm.patchValue({
      id: candidate.id,
      fullName: candidate.fullName,
      email: candidate.email,
      cvUrl: candidate.cvUrl,
      vacancies: candidate.vacancies
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidate = this.createFromForm();
    if (candidate.id !== undefined) {
      this.subscribeToSaveResponse(this.candidateService.update(candidate));
    } else {
      this.subscribeToSaveResponse(this.candidateService.create(candidate));
    }
  }

  private createFromForm(): ICandidate {
    return {
      ...new Candidate(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      email: this.editForm.get(['email'])!.value,
      cvUrl: this.editForm.get(['cvUrl'])!.value,
      vacancies: this.editForm.get(['vacancies'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidate>>): void {
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

  getSelected(selectedVals: IVacancy[], option: IVacancy): IVacancy {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
