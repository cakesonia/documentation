import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { InterviewResultDetailComponent } from 'app/entities/interview-result/interview-result-detail.component';
import { InterviewResult } from 'app/shared/model/interview-result.model';

describe('Component Tests', () => {
  describe('InterviewResult Management Detail Component', () => {
    let comp: InterviewResultDetailComponent;
    let fixture: ComponentFixture<InterviewResultDetailComponent>;
    const route = ({ data: of({ interviewResult: new InterviewResult(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewResultDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InterviewResultDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterviewResultDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load interviewResult on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.interviewResult).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
