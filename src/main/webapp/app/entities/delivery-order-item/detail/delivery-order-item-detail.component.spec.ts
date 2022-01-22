import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliveryOrderItemDetailComponent } from './delivery-order-item-detail.component';

describe('DeliveryOrderItem Management Detail Component', () => {
  let comp: DeliveryOrderItemDetailComponent;
  let fixture: ComponentFixture<DeliveryOrderItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeliveryOrderItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deliveryOrderItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeliveryOrderItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeliveryOrderItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deliveryOrderItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deliveryOrderItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
