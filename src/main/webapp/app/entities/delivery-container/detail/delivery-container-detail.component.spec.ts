import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeliveryContainerDetailComponent } from './delivery-container-detail.component';

describe('DeliveryContainer Management Detail Component', () => {
  let comp: DeliveryContainerDetailComponent;
  let fixture: ComponentFixture<DeliveryContainerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeliveryContainerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deliveryContainer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeliveryContainerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeliveryContainerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deliveryContainer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deliveryContainer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
