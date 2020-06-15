import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IApplicationStatus, ApplicationStatus } from 'app/shared/model/application-status.model';
import { ApplicationStatusService } from './application-status.service';
import { ApplicationStatusComponent } from './application-status.component';
import { ApplicationStatusDetailComponent } from './application-status-detail.component';
import { ApplicationStatusUpdateComponent } from './application-status-update.component';

@Injectable({ providedIn: 'root' })
export class ApplicationStatusResolve implements Resolve<IApplicationStatus> {
  constructor(private service: ApplicationStatusService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApplicationStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((applicationStatus: HttpResponse<ApplicationStatus>) => {
          if (applicationStatus.body) {
            return of(applicationStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApplicationStatus());
  }
}

export const applicationStatusRoute: Routes = [
  {
    path: '',
    component: ApplicationStatusComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ApplicationStatuses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ApplicationStatusDetailComponent,
    resolve: {
      applicationStatus: ApplicationStatusResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ApplicationStatuses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ApplicationStatusUpdateComponent,
    resolve: {
      applicationStatus: ApplicationStatusResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ApplicationStatuses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ApplicationStatusUpdateComponent,
    resolve: {
      applicationStatus: ApplicationStatusResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ApplicationStatuses'
    },
    canActivate: [UserRouteAccessService]
  }
];
