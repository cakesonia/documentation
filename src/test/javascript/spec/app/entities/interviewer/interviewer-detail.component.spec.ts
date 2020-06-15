import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { InterviewerDetailComponent } from 'app/entities/interviewer/interviewer-detail.component';
import { Interviewer } from 'app/shared/model/interviewer.model';

describe('Component Tests', () => {
  describe('Interviewer Management Detail Component', () => {
    let comp: InterviewerDetailComponent;
    let fixture: ComponentFixture<InterviewerDetailComponent>;
    const route = ({ data: of({ interviewer: new Interviewer(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InterviewerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterviewerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load interviewer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.interviewer).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
