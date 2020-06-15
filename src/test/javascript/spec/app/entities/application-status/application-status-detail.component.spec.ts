import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { ApplicationStatusDetailComponent } from 'app/entities/application-status/application-status-detail.component';
import { ApplicationStatus } from 'app/shared/model/application-status.model';

describe('Component Tests', () => {
  describe('ApplicationStatus Management Detail Component', () => {
    let comp: ApplicationStatusDetailComponent;
    let fixture: ComponentFixture<ApplicationStatusDetailComponent>;
    const route = ({ data: of({ applicationStatus: new ApplicationStatus(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [ApplicationStatusDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ApplicationStatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApplicationStatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load applicationStatus on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.applicationStatus).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
