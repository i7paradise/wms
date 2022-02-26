import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderItemProductService } from '../service/order-item-product.service';
import { IOrderItemProduct, OrderItemProduct } from '../order-item-product.model';
import { IOrderContainer } from 'app/entities/order-container/order-container.model';
import { OrderContainerService } from 'app/entities/order-container/service/order-container.service';

import { OrderItemProductUpdateComponent } from './order-item-product-update.component';

describe('OrderItemProduct Management Update Component', () => {
  let comp: OrderItemProductUpdateComponent;
  let fixture: ComponentFixture<OrderItemProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderItemProductService: OrderItemProductService;
  let orderContainerService: OrderContainerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrderItemProductUpdateComponent],
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
      .overrideTemplate(OrderItemProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderItemProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderItemProductService = TestBed.inject(OrderItemProductService);
    orderContainerService = TestBed.inject(OrderContainerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call OrderContainer query and add missing value', () => {
      const orderItemProduct: IOrderItemProduct = { id: 456 };
      const orderContainer: IOrderContainer = { id: 13366 };
      orderItemProduct.orderContainer = orderContainer;

      const orderContainerCollection: IOrderContainer[] = [{ id: 90464 }];
      jest.spyOn(orderContainerService, 'query').mockReturnValue(of(new HttpResponse({ body: orderContainerCollection })));
      const additionalOrderContainers = [orderContainer];
      const expectedCollection: IOrderContainer[] = [...additionalOrderContainers, ...orderContainerCollection];
      jest.spyOn(orderContainerService, 'addOrderContainerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItemProduct });
      comp.ngOnInit();

      expect(orderContainerService.query).toHaveBeenCalled();
      expect(orderContainerService.addOrderContainerToCollectionIfMissing).toHaveBeenCalledWith(
        orderContainerCollection,
        ...additionalOrderContainers
      );
      expect(comp.orderContainersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orderItemProduct: IOrderItemProduct = { id: 456 };
      const orderContainer: IOrderContainer = { id: 52780 };
      orderItemProduct.orderContainer = orderContainer;

      activatedRoute.data = of({ orderItemProduct });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(orderItemProduct));
      expect(comp.orderContainersSharedCollection).toContain(orderContainer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderItemProduct>>();
      const orderItemProduct = { id: 123 };
      jest.spyOn(orderItemProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderItemProduct }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderItemProductService.update).toHaveBeenCalledWith(orderItemProduct);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderItemProduct>>();
      const orderItemProduct = new OrderItemProduct();
      jest.spyOn(orderItemProductService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderItemProduct }));
      saveSubject.complete();

      // THEN
      expect(orderItemProductService.create).toHaveBeenCalledWith(orderItemProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderItemProduct>>();
      const orderItemProduct = { id: 123 };
      jest.spyOn(orderItemProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItemProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderItemProductService.update).toHaveBeenCalledWith(orderItemProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOrderContainerById', () => {
      it('Should return tracked OrderContainer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrderContainerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
