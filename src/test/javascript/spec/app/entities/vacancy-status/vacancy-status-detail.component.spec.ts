import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Lab3TestModule } from '../../../test.module';
import { VacancyStatusDetailComponent } from 'app/entities/vacancy-status/vacancy-status-detail.component';
import { VacancyStatus } from 'app/shared/model/vacancy-status.model';

describe('Component Tests', () => {
  describe('VacancyStatus Management Detail Component', () => {
    let comp: VacancyStatusDetailComponent;
    let fixture: ComponentFixture<VacancyStatusDetailComponent>;
    const route = ({ data: of({ vacancyStatus: new VacancyStatus(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Lab3TestModule],
        declarations: [VacancyStatusDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(VacancyStatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VacancyStatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vacancyStatus on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vacancyStatus).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
