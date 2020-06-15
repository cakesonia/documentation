import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { InterviewUpdateComponent } from 'app/entities/interview/interview-update.component';
import { InterviewService } from 'app/entities/interview/interview.service';
import { Interview } from 'app/shared/model/interview.model';

describe('Component Tests', () => {
  describe('Interview Management Update Component', () => {
    let comp: InterviewUpdateComponent;
    let fixture: ComponentFixture<InterviewUpdateComponent>;
    let service: InterviewService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InterviewUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterviewUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterviewService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Interview(123);
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
        const entity = new Interview();
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
