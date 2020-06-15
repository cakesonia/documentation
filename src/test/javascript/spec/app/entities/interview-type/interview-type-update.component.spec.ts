import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { InterviewTypeUpdateComponent } from 'app/entities/interview-type/interview-type-update.component';
import { InterviewTypeService } from 'app/entities/interview-type/interview-type.service';
import { InterviewType } from 'app/shared/model/interview-type.model';

describe('Component Tests', () => {
  describe('InterviewType Management Update Component', () => {
    let comp: InterviewTypeUpdateComponent;
    let fixture: ComponentFixture<InterviewTypeUpdateComponent>;
    let service: InterviewTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InterviewTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterviewTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterviewTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new InterviewType(123);
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
        const entity = new InterviewType();
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
