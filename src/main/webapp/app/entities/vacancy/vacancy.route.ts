import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVacancy, Vacancy } from 'app/shared/model/vacancy.model';
import { VacancyService } from './vacancy.service';
import { VacancyComponent } from './vacancy.component';
import { VacancyDetailComponent } from './vacancy-detail.component';
import { VacancyUpdateComponent } from './vacancy-update.component';

@Injectable({ providedIn: 'root' })
export class VacancyResolve implements Resolve<IVacancy> {
  constructor(private service: VacancyService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVacancy> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vacancy: HttpResponse<Vacancy>) => {
          if (vacancy.body) {
            return of(vacancy.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vacancy());
  }
}

export const vacancyRoute: Routes = [
  {
    path: '',
    component: VacancyComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Vacancies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VacancyDetailComponent,
    resolve: {
      vacancy: VacancyResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Vacancies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VacancyUpdateComponent,
    resolve: {
      vacancy: VacancyResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Vacancies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VacancyUpdateComponent,
    resolve: {
      vacancy: VacancyResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Vacancies'
    },
    canActivate: [UserRouteAccessService]
  }
];
