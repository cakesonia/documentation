import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Lab3TestModule } from '../../../test.module';
import { InterviewResultComponent } from 'app/entities/interview-result/interview-result.component';
import { InterviewResultService } from 'app/entities/interview-result/interview-result.service';
import { InterviewResult } from 'app/shared/model/interview-result.model';

describe('Component Tests', () => {
  describe('InterviewResult Management Component', () => {
    let comp: InterviewResultComponent;
    let fixture: ComponentFixture<InterviewResultComponent>;
    let service: InterviewResultService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewResultComponent]
      })
        .overrideTemplate(InterviewResultComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterviewResultComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterviewResultService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new InterviewResult(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.interviewResults && comp.interviewResults[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
