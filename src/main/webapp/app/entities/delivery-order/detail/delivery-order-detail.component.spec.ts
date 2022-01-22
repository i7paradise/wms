import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliveryOrderDetailComponent } from './delivery-order-detail.component';

describe('DeliveryOrder Management Detail Component', () => {
  let comp: DeliveryOrderDetailComponent;
  let fixture: ComponentFixture<DeliveryOrderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeliveryOrderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deliveryOrder: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeliveryOrderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeliveryOrderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deliveryOrder on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deliveryOrder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
