import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderItemProductDetailComponent } from './order-item-product-detail.component';

describe('OrderItemProduct Management Detail Component', () => {
  let comp: OrderItemProductDetailComponent;
  let fixture: ComponentFixture<OrderItemProductDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderItemProductDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ orderItemProduct: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrderItemProductDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrderItemProductDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load orderItemProduct on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.orderItemProduct).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
