import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInterviewResult, InterviewResult } from 'app/shared/model/interview-result.model';
import { InterviewResultService } from './interview-result.service';
import { InterviewResultComponent } from './interview-result.component';
import { InterviewResultDetailComponent } from './interview-result-detail.component';
import { InterviewResultUpdateComponent } from './interview-result-update.component';

@Injectable({ providedIn: 'root' })
export class InterviewResultResolve implements Resolve<IInterviewResult> {
  constructor(private service: InterviewResultService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInterviewResult> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((interviewResult: HttpResponse<InterviewResult>) => {
          if (interviewResult.body) {
            return of(interviewResult.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InterviewResult());
  }
}

export const interviewResultRoute: Routes = [
  {
    path: '',
    component: InterviewResultComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'InterviewResults'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InterviewResultDetailComponent,
    resolve: {
      interviewResult: InterviewResultResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'InterviewResults'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InterviewResultUpdateComponent,
    resolve: {
      interviewResult: InterviewResultResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'InterviewResults'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InterviewResultUpdateComponent,
    resolve: {
      interviewResult: InterviewResultResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'InterviewResults'
    },
    canActivate: [UserRouteAccessService]
  }
];
