import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShippingDetailComponent } from './shipping-detail.component';

describe('ShippingDetailComponent', () => {
  let comp: ShippingDetailComponent;
  let fixture: ComponentFixture<ShippingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShippingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ order: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ShippingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ShippingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load order on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.order).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
