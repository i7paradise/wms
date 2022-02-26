import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderContainerDetailComponent } from './order-container-detail.component';

describe('OrderContainer Management Detail Component', () => {
  let comp: OrderContainerDetailComponent;
  let fixture: ComponentFixture<OrderContainerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderContainerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ orderContainer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrderContainerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrderContainerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load orderContainer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.orderContainer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
