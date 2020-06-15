import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInterview, Interview } from 'app/shared/model/interview.model';
import { InterviewService } from './interview.service';
import { InterviewComponent } from './interview.component';
import { InterviewDetailComponent } from './interview-detail.component';
import { InterviewUpdateComponent } from './interview-update.component';

@Injectable({ providedIn: 'root' })
export class InterviewResolve implements Resolve<IInterview> {
  constructor(private service: InterviewService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInterview> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((interview: HttpResponse<Interview>) => {
          if (interview.body) {
            return of(interview.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Interview());
  }
}

export const interviewRoute: Routes = [
  {
    path: '',
    component: InterviewComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Interviews'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InterviewDetailComponent,
    resolve: {
      interview: InterviewResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Interviews'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InterviewUpdateComponent,
    resolve: {
      interview: InterviewResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Interviews'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InterviewUpdateComponent,
    resolve: {
      interview: InterviewResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Interviews'
    },
    canActivate: [UserRouteAccessService]
  }
];
