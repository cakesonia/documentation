import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { InterviewTypeDetailComponent } from 'app/entities/interview-type/interview-type-detail.component';
import { InterviewType } from 'app/shared/model/interview-type.model';

describe('Component Tests', () => {
  describe('InterviewType Management Detail Component', () => {
    let comp: InterviewTypeDetailComponent;
    let fixture: ComponentFixture<InterviewTypeDetailComponent>;
    const route = ({ data: of({ interviewType: new InterviewType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InterviewTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterviewTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load interviewType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.interviewType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
