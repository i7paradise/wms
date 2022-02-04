import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderItemProductService } from '../service/order-item-product.service';
import { IOrderItemProduct, OrderItemProduct } from '../order-item-product.model';
import { IContainerCategory } from 'app/entities/container-category/container-category.model';
import { ContainerCategoryService } from 'app/entities/container-category/service/container-category.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';

import { OrderItemProductUpdateComponent } from './order-item-product-update.component';

describe('OrderItemProduct Management Update Component', () => {
  let comp: OrderItemProductUpdateComponent;
  let fixture: ComponentFixture<OrderItemProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderItemProductService: OrderItemProductService;
  let containerCategoryService: ContainerCategoryService;
  let orderItemService: OrderItemService;

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
    containerCategoryService = TestBed.inject(ContainerCategoryService);
    orderItemService = TestBed.inject(OrderItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ContainerCategory query and add missing value', () => {
      const orderItemProduct: IOrderItemProduct = { id: 456 };
      const containerCategory: IContainerCategory = { id: 71591 };
      orderItemProduct.containerCategory = containerCategory;

      const containerCategoryCollection: IContainerCategory[] = [{ id: 8325 }];
      jest.spyOn(containerCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: containerCategoryCollection })));
      const additionalContainerCategories = [containerCategory];
      const expectedCollection: IContainerCategory[] = [...additionalContainerCategories, ...containerCategoryCollection];
      jest.spyOn(containerCategoryService, 'addContainerCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItemProduct });
      comp.ngOnInit();

      expect(containerCategoryService.query).toHaveBeenCalled();
      expect(containerCategoryService.addContainerCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        containerCategoryCollection,
        ...additionalContainerCategories
      );
      expect(comp.containerCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call OrderItem query and add missing value', () => {
      const orderItemProduct: IOrderItemProduct = { id: 456 };
      const orderItem: IOrderItem = { id: 82141 };
      orderItemProduct.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 3948 }];
      jest.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      jest.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItemProduct });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(orderItemCollection, ...additionalOrderItems);
      expect(comp.orderItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orderItemProduct: IOrderItemProduct = { id: 456 };
      const containerCategory: IContainerCategory = { id: 26639 };
      orderItemProduct.containerCategory = containerCategory;
      const orderItem: IOrderItem = { id: 92801 };
      orderItemProduct.orderItem = orderItem;

      activatedRoute.data = of({ orderItemProduct });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(orderItemProduct));
      expect(comp.containerCategoriesSharedCollection).toContain(containerCategory);
      expect(comp.orderItemsSharedCollection).toContain(orderItem);
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
    describe('trackContainerCategoryById', () => {
      it('Should return tracked ContainerCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContainerCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOrderItemById', () => {
      it('Should return tracked OrderItem primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrderItemById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
