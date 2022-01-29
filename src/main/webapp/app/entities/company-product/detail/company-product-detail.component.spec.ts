import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompanyProductDetailComponent } from './company-product-detail.component';

describe('CompanyProduct Management Detail Component', () => {
  let comp: CompanyProductDetailComponent;
  let fixture: ComponentFixture<CompanyProductDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompanyProductDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ companyProduct: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompanyProductDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompanyProductDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load companyProduct on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.companyProduct).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
