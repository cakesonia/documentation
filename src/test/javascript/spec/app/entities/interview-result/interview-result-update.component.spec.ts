import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { InterviewResultUpdateComponent } from 'app/entities/interview-result/interview-result-update.component';
import { InterviewResultService } from 'app/entities/interview-result/interview-result.service';
import { InterviewResult } from 'app/shared/model/interview-result.model';

describe('Component Tests', () => {
  describe('InterviewResult Management Update Component', () => {
    let comp: InterviewResultUpdateComponent;
    let fixture: ComponentFixture<InterviewResultUpdateComponent>;
    let service: InterviewResultService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewResultUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InterviewResultUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterviewResultUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterviewResultService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new InterviewResult(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new InterviewResult();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
