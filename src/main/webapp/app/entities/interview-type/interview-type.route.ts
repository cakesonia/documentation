import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInterviewType, InterviewType } from 'app/shared/model/interview-type.model';
import { InterviewTypeService } from './interview-type.service';
import { InterviewTypeComponent } from './interview-type.component';
import { InterviewTypeDetailComponent } from './interview-type-detail.component';
import { InterviewTypeUpdateComponent } from './interview-type-update.component';

@Injectable({ providedIn: 'root' })
export class InterviewTypeResolve implements Resolve<IInterviewType> {
  constructor(private service: InterviewTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInterviewType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((interviewType: HttpResponse<InterviewType>) => {
          if (interviewType.body) {
            return of(interviewType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InterviewType());
  }
}

export const interviewTypeRoute: Routes = [
  {
    path: '',
    component: InterviewTypeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'InterviewTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InterviewTypeDetailComponent,
    resolve: {
      interviewType: InterviewTypeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'InterviewTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InterviewTypeUpdateComponent,
    resolve: {
      interviewType: InterviewTypeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'InterviewTypes'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InterviewTypeUpdateComponent,
    resolve: {
      interviewType: InterviewTypeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'InterviewTypes'
    },
    canActivate: [UserRouteAccessService]
  }
];
