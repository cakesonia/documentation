import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Lab3TestModule } from '../../../test.module';
import { VacancyStatusComponent } from 'app/entities/vacancy-status/vacancy-status.component';
import { VacancyStatusService } from 'app/entities/vacancy-status/vacancy-status.service';
import { VacancyStatus } from 'app/shared/model/vacancy-status.model';

describe('Component Tests', () => {
  describe('VacancyStatus Management Component', () => {
    let comp: VacancyStatusComponent;
    let fixture: ComponentFixture<VacancyStatusComponent>;
    let service: VacancyStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [VacancyStatusComponent]
      })
        .overrideTemplate(VacancyStatusComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VacancyStatusComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VacancyStatusService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new VacancyStatus(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.vacancyStatuses && comp.vacancyStatuses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
