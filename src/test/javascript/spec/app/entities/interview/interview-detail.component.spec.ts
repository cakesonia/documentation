import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { InterviewDetailComponent } from 'app/entities/interview/interview-detail.component';
import { Interview } from 'app/shared/model/interview.model';

describe('Component Tests', () => {
  describe('Interview Management Detail Component', () => {
    let comp: InterviewDetailComponent;
    let fixture: ComponentFixture<InterviewDetailComponent>;
    const route = ({ data: of({ interview: new Interview(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InterviewDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterviewDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load interview on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.interview).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
