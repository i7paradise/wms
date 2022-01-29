import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompanyContainerDetailComponent } from './company-container-detail.component';

describe('CompanyContainer Management Detail Component', () => {
  let comp: CompanyContainerDetailComponent;
  let fixture: ComponentFixture<CompanyContainerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyContainerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ companyContainer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompanyContainerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompanyContainerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load companyContainer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.companyContainer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
