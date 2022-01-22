import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeliveryOrderItemService } from '../service/delivery-order-item.service';
import { IDeliveryOrderItem, DeliveryOrderItem } from '../delivery-order-item.model';
import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { CompanyProductService } from 'app/entities/company-product/service/company-product.service';
import { IDeliveryOrder } from 'app/entities/delivery-order/delivery-order.model';
import { DeliveryOrderService } from 'app/entities/delivery-order/service/delivery-order.service';

import { DeliveryOrderItemUpdateComponent } from './delivery-order-item-update.component';

describe('DeliveryOrderItem Management Update Component', () => {
  let comp: DeliveryOrderItemUpdateComponent;
  let fixture: ComponentFixture<DeliveryOrderItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deliveryOrderItemService: DeliveryOrderItemService;
  let companyProductService: CompanyProductService;
  let deliveryOrderService: DeliveryOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeliveryOrderItemUpdateComponent],
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
      .overrideTemplate(DeliveryOrderItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliveryOrderItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deliveryOrderItemService = TestBed.inject(DeliveryOrderItemService);
    companyProductService = TestBed.inject(CompanyProductService);
    deliveryOrderService = TestBed.inject(DeliveryOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call compganyProduct query and add missing value', () => {
      const deliveryOrderItem: IDeliveryOrderItem = { id: 456 };
      const compganyProduct: ICompanyProduct = { id: 36921 };
      deliveryOrderItem.compganyProduct = compganyProduct;

      const compganyProductCollection: ICompanyProduct[] = [{ id: 53513 }];
      jest.spyOn(companyProductService, 'query').mockReturnValue(of(new HttpResponse({ body: compganyProductCollection })));
      const expectedCollection: ICompanyProduct[] = [compganyProduct, ...compganyProductCollection];
      jest.spyOn(companyProductService, 'addCompanyProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryOrderItem });
      comp.ngOnInit();

      expect(companyProductService.query).toHaveBeenCalled();
      expect(companyProductService.addCompanyProductToCollectionIfMissing).toHaveBeenCalledWith(compganyProductCollection, compganyProduct);
      expect(comp.compganyProductsCollection).toEqual(expectedCollection);
    });

    it('Should call DeliveryOrder query and add missing value', () => {
      const deliveryOrderItem: IDeliveryOrderItem = { id: 456 };
      const deliveryOrder: IDeliveryOrder = { id: 49936 };
      deliveryOrderItem.deliveryOrder = deliveryOrder;

      const deliveryOrderCollection: IDeliveryOrder[] = [{ id: 51228 }];
      jest.spyOn(deliveryOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryOrderCollection })));
      const additionalDeliveryOrders = [deliveryOrder];
      const expectedCollection: IDeliveryOrder[] = [...additionalDeliveryOrders, ...deliveryOrderCollection];
      jest.spyOn(deliveryOrderService, 'addDeliveryOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryOrderItem });
      comp.ngOnInit();

      expect(deliveryOrderService.query).toHaveBeenCalled();
      expect(deliveryOrderService.addDeliveryOrderToCollectionIfMissing).toHaveBeenCalledWith(
        deliveryOrderCollection,
        ...additionalDeliveryOrders
      );
      expect(comp.deliveryOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deliveryOrderItem: IDeliveryOrderItem = { id: 456 };
      const compganyProduct: ICompanyProduct = { id: 7896 };
      deliveryOrderItem.compganyProduct = compganyProduct;
      const deliveryOrder: IDeliveryOrder = { id: 90806 };
      deliveryOrderItem.deliveryOrder = deliveryOrder;

      activatedRoute.data = of({ deliveryOrderItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deliveryOrderItem));
      expect(comp.compganyProductsCollection).toContain(compganyProduct);
      expect(comp.deliveryOrdersSharedCollection).toContain(deliveryOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryOrderItem>>();
      const deliveryOrderItem = { id: 123 };
      jest.spyOn(deliveryOrderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryOrderItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deliveryOrderItemService.update).toHaveBeenCalledWith(deliveryOrderItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryOrderItem>>();
      const deliveryOrderItem = new DeliveryOrderItem();
      jest.spyOn(deliveryOrderItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryOrderItem }));
      saveSubject.complete();

      // THEN
      expect(deliveryOrderItemService.create).toHaveBeenCalledWith(deliveryOrderItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryOrderItem>>();
      const deliveryOrderItem = { id: 123 };
      jest.spyOn(deliveryOrderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deliveryOrderItemService.update).toHaveBeenCalledWith(deliveryOrderItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCompanyProductById', () => {
      it('Should return tracked CompanyProduct primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompanyProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDeliveryOrderById', () => {
      it('Should return tracked DeliveryOrder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDeliveryOrderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
