import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Lab3TestModule } from '../../../test.module';
import { InterviewTypeComponent } from 'app/entities/interview-type/interview-type.component';
import { InterviewTypeService } from 'app/entities/interview-type/interview-type.service';
import { InterviewType } from 'app/shared/model/interview-type.model';

describe('Component Tests', () => {
  describe('InterviewType Management Component', () => {
    let comp: InterviewTypeComponent;
    let fixture: ComponentFixture<InterviewTypeComponent>;
    let service: InterviewTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewTypeComponent]
      })
        .overrideTemplate(InterviewTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterviewTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterviewTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new InterviewType(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.interviewTypes && comp.interviewTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
