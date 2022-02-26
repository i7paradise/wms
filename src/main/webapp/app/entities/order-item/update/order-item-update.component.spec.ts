import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderItemService } from '../service/order-item.service';
import { IOrderItem, OrderItem } from '../order-item.model';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { CompanyProductService } from 'app/entities/company-product/service/company-product.service';

import { OrderItemUpdateComponent } from './order-item-update.component';

describe('OrderItem Management Update Component', () => {
  let comp: OrderItemUpdateComponent;
  let fixture: ComponentFixture<OrderItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderItemService: OrderItemService;
  let orderService: OrderService;
  let companyProductService: CompanyProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrderItemUpdateComponent],
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
      .overrideTemplate(OrderItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderItemService = TestBed.inject(OrderItemService);
    orderService = TestBed.inject(OrderService);
    companyProductService = TestBed.inject(CompanyProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Order query and add missing value', () => {
      const orderItem: IOrderItem = { id: 456 };
      const order: IOrder = { id: 39120 };
      orderItem.order = order;

      const orderCollection: IOrder[] = [{ id: 6598 }];
      jest.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
      const additionalOrders = [order];
      const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
      jest.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(orderService.query).toHaveBeenCalled();
      expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(orderCollection, ...additionalOrders);
      expect(comp.ordersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CompanyProduct query and add missing value', () => {
      const orderItem: IOrderItem = { id: 456 };
      const companyProduct: ICompanyProduct = { id: 82230 };
      orderItem.companyProduct = companyProduct;

      const companyProductCollection: ICompanyProduct[] = [{ id: 78496 }];
      jest.spyOn(companyProductService, 'query').mockReturnValue(of(new HttpResponse({ body: companyProductCollection })));
      const additionalCompanyProducts = [companyProduct];
      const expectedCollection: ICompanyProduct[] = [...additionalCompanyProducts, ...companyProductCollection];
      jest.spyOn(companyProductService, 'addCompanyProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(companyProductService.query).toHaveBeenCalled();
      expect(companyProductService.addCompanyProductToCollectionIfMissing).toHaveBeenCalledWith(
        companyProductCollection,
        ...additionalCompanyProducts
      );
      expect(comp.companyProductsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orderItem: IOrderItem = { id: 456 };
      const order: IOrder = { id: 30033 };
      orderItem.order = order;
      const companyProduct: ICompanyProduct = { id: 19745 };
      orderItem.companyProduct = companyProduct;

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(orderItem));
      expect(comp.ordersSharedCollection).toContain(order);
      expect(comp.companyProductsSharedCollection).toContain(companyProduct);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderItem>>();
      const orderItem = { id: 123 };
      jest.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderItemService.update).toHaveBeenCalledWith(orderItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderItem>>();
      const orderItem = new OrderItem();
      jest.spyOn(orderItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderItem }));
      saveSubject.complete();

      // THEN
      expect(orderItemService.create).toHaveBeenCalledWith(orderItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderItem>>();
      const orderItem = { id: 123 };
      jest.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderItemService.update).toHaveBeenCalledWith(orderItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOrderById', () => {
      it('Should return tracked Order primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCompanyProductById', () => {
      it('Should return tracked CompanyProduct primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompanyProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
