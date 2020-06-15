import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInterviewer, Interviewer } from 'app/shared/model/interviewer.model';
import { InterviewerService } from './interviewer.service';
import { InterviewerComponent } from './interviewer.component';
import { InterviewerDetailComponent } from './interviewer-detail.component';
import { InterviewerUpdateComponent } from './interviewer-update.component';

@Injectable({ providedIn: 'root' })
export class InterviewerResolve implements Resolve<IInterviewer> {
  constructor(private service: InterviewerService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInterviewer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((interviewer: HttpResponse<Interviewer>) => {
          if (interviewer.body) {
            return of(interviewer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Interviewer());
  }
}

export const interviewerRoute: Routes = [
  {
    path: '',
    component: InterviewerComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Interviewers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InterviewerDetailComponent,
    resolve: {
      interviewer: InterviewerResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Interviewers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InterviewerUpdateComponent,
    resolve: {
      interviewer: InterviewerResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Interviewers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InterviewerUpdateComponent,
    resolve: {
      interviewer: InterviewerResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Interviewers'
    },
    canActivate: [UserRouteAccessService]
  }
];
