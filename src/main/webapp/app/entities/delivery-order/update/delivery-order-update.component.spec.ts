import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeliveryOrderService } from '../service/delivery-order.service';
import { IDeliveryOrder, DeliveryOrder } from '../delivery-order.model';

import { DeliveryOrderUpdateComponent } from './delivery-order-update.component';

describe('DeliveryOrder Management Update Component', () => {
  let comp: DeliveryOrderUpdateComponent;
  let fixture: ComponentFixture<DeliveryOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deliveryOrderService: DeliveryOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeliveryOrderUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DeliveryOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliveryOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deliveryOrderService = TestBed.inject(DeliveryOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const deliveryOrder: IDeliveryOrder = { id: 456 };

      activatedRoute.data = of({ deliveryOrder });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deliveryOrder));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryOrder>>();
      const deliveryOrder = { id: 123 };
      jest.spyOn(deliveryOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryOrder }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deliveryOrderService.update).toHaveBeenCalledWith(deliveryOrder);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryOrder>>();
      const deliveryOrder = new DeliveryOrder();
      jest.spyOn(deliveryOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryOrder }));
      saveSubject.complete();

      // THEN
      expect(deliveryOrderService.create).toHaveBeenCalledWith(deliveryOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryOrder>>();
      const deliveryOrder = { id: 123 };
      jest.spyOn(deliveryOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deliveryOrderService.update).toHaveBeenCalledWith(deliveryOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
