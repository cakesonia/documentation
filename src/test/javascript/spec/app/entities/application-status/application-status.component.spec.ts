import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Lab3TestModule } from '../../../test.module';
import { ApplicationStatusComponent } from 'app/entities/application-status/application-status.component';
import { ApplicationStatusService } from 'app/entities/application-status/application-status.service';
import { ApplicationStatus } from 'app/shared/model/application-status.model';

describe('Component Tests', () => {
  describe('ApplicationStatus Management Component', () => {
    let comp: ApplicationStatusComponent;
    let fixture: ComponentFixture<ApplicationStatusComponent>;
    let service: ApplicationStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [ApplicationStatusComponent]
      })
        .overrideTemplate(ApplicationStatusComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicationStatusComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApplicationStatusService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ApplicationStatus(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.applicationStatuses && comp.applicationStatuses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
