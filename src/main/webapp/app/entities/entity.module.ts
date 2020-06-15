import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'candidate',
        loadChildren: () => import('./candidate/candidate.module').then(m => m.Lab3CandidateModule)
      },
      {
        path: 'vacancy-status',
        loadChildren: () => import('./vacancy-status/vacancy-status.module').then(m => m.Lab3VacancyStatusModule)
      },
      {
        path: 'application-status',
        loadChildren: () => import('./application-status/application-status.module').then(m => m.Lab3ApplicationStatusModule)
      },
      {
        path: 'vacancy',
        loadChildren: () => import('./vacancy/vacancy.module').then(m => m.Lab3VacancyModule)
      },
      {
        path: 'application',
        loadChildren: () => import('./application/application.module').then(m => m.Lab3ApplicationModule)
      },
      {
        path: 'interview',
        loadChildren: () => import('./interview/interview.module').then(m => m.Lab3InterviewModule)
      },
      {
        path: 'interview-type',
        loadChildren: () => import('./interview-type/interview-type.module').then(m => m.Lab3InterviewTypeModule)
      },
      {
        path: 'interviewer',
        loadChildren: () => import('./interviewer/interviewer.module').then(m => m.Lab3InterviewerModule)
      },
      {
        path: 'interview-result',
        loadChildren: () => import('./interview-result/interview-result.module').then(m => m.Lab3InterviewResultModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class Lab3EntityModule {}
