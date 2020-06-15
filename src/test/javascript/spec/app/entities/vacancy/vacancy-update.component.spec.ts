import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { VacancyUpdateComponent } from 'app/entities/vacancy/vacancy-update.component';
import { VacancyService } from 'app/entities/vacancy/vacancy.service';
import { Vacancy } from 'app/shared/model/vacancy.model';

describe('Component Tests', () => {
  describe('Vacancy Management Update Component', () => {
    let comp: VacancyUpdateComponent;
    let fixture: ComponentFixture<VacancyUpdateComponent>;
    let service: VacancyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [VacancyUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VacancyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VacancyUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VacancyService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Vacancy(123);
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
        const entity = new Vacancy();
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
