import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { ApplicationStatusUpdateComponent } from 'app/entities/application-status/application-status-update.component';
import { ApplicationStatusService } from 'app/entities/application-status/application-status.service';
import { ApplicationStatus } from 'app/shared/model/application-status.model';

describe('Component Tests', () => {
  describe('ApplicationStatus Management Update Component', () => {
    let comp: ApplicationStatusUpdateComponent;
    let fixture: ComponentFixture<ApplicationStatusUpdateComponent>;
    let service: ApplicationStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [ApplicationStatusUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ApplicationStatusUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicationStatusUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApplicationStatusService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ApplicationStatus(123);
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
        const entity = new ApplicationStatus();
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
