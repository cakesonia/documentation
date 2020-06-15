import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { ApplicationDetailComponent } from 'app/entities/application/application-detail.component';
import { Application } from 'app/shared/model/application.model';

describe('Component Tests', () => {
  describe('Application Management Detail Component', () => {
    let comp: ApplicationDetailComponent;
    let fixture: ComponentFixture<ApplicationDetailComponent>;
    const route = ({ data: of({ application: new Application(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [ApplicationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ApplicationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApplicationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load application on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.application).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
