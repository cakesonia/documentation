import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVacancyStatus, VacancyStatus } from 'app/shared/model/vacancy-status.model';
import { VacancyStatusService } from './vacancy-status.service';
import { VacancyStatusComponent } from './vacancy-status.component';
import { VacancyStatusDetailComponent } from './vacancy-status-detail.component';
import { VacancyStatusUpdateComponent } from './vacancy-status-update.component';

@Injectable({ providedIn: 'root' })
export class VacancyStatusResolve implements Resolve<IVacancyStatus> {
  constructor(private service: VacancyStatusService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVacancyStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vacancyStatus: HttpResponse<VacancyStatus>) => {
          if (vacancyStatus.body) {
            return of(vacancyStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VacancyStatus());
  }
}

export const vacancyStatusRoute: Routes = [
  {
    path: '',
    component: VacancyStatusComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'VacancyStatuses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VacancyStatusDetailComponent,
    resolve: {
      vacancyStatus: VacancyStatusResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'VacancyStatuses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VacancyStatusUpdateComponent,
    resolve: {
      vacancyStatus: VacancyStatusResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'VacancyStatuses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VacancyStatusUpdateComponent,
    resolve: {
      vacancyStatus: VacancyStatusResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'VacancyStatuses'
    },
    canActivate: [UserRouteAccessService]
  }
];
