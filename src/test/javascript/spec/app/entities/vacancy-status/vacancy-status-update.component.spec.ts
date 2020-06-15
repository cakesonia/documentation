import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { VacancyStatusUpdateComponent } from 'app/entities/vacancy-status/vacancy-status-update.component';
import { VacancyStatusService } from 'app/entities/vacancy-status/vacancy-status.service';
import { VacancyStatus } from 'app/shared/model/vacancy-status.model';

describe('Component Tests', () => {
  describe('VacancyStatus Management Update Component', () => {
    let comp: VacancyStatusUpdateComponent;
    let fixture: ComponentFixture<VacancyStatusUpdateComponent>;
    let service: VacancyStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [VacancyStatusUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VacancyStatusUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VacancyStatusUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VacancyStatusService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new VacancyStatus(123);
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
        const entity = new VacancyStatus();
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
