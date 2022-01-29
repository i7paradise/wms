import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliveryItemProductDetailComponent } from './delivery-item-product-detail.component';

describe('DeliveryItemProduct Management Detail Component', () => {
  let comp: DeliveryItemProductDetailComponent;
  let fixture: ComponentFixture<DeliveryItemProductDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeliveryItemProductDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deliveryItemProduct: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeliveryItemProductDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeliveryItemProductDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deliveryItemProduct on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deliveryItemProduct).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
