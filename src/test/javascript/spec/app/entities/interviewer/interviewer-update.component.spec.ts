import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { InterviewerUpdateComponent } from 'app/entities/interviewer/interviewer-update.component';
import { InterviewerService } from 'app/entities/interviewer/interviewer.service';
import { Interviewer } from 'app/shared/model/interviewer.model';

describe('Component Tests', () => {
  describe('Interviewer Management Update Component', () => {
    let comp: InterviewerUpdateComponent;
    let fixture: ComponentFixture<InterviewerUpdateComponent>;
    let service: InterviewerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [InterviewerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InterviewerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterviewerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterviewerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Interviewer(123);
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
        const entity = new Interviewer();
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
