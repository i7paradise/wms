import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderContainerService } from '../service/order-container.service';
import { IOrderContainer, OrderContainer } from '../order-container.model';
import { ICompanyContainer } from 'app/entities/company-container/company-container.model';
import { CompanyContainerService } from 'app/entities/company-container/service/company-container.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';

import { OrderContainerUpdateComponent } from './order-container-update.component';

describe('OrderContainer Management Update Component', () => {
  let comp: OrderContainerUpdateComponent;
  let fixture: ComponentFixture<OrderContainerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderContainerService: OrderContainerService;
  let companyContainerService: CompanyContainerService;
  let orderItemService: OrderItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrderContainerUpdateComponent],
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
      .overrideTemplate(OrderContainerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderContainerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderContainerService = TestBed.inject(OrderContainerService);
    companyContainerService = TestBed.inject(CompanyContainerService);
    orderItemService = TestBed.inject(OrderItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call companyContainer query and add missing value', () => {
      const orderContainer: IOrderContainer = { id: 456 };
      const companyContainer: ICompanyContainer = { id: 53161 };
      orderContainer.companyContainer = companyContainer;

      const companyContainerCollection: ICompanyContainer[] = [{ id: 7255 }];
      jest.spyOn(companyContainerService, 'query').mockReturnValue(of(new HttpResponse({ body: companyContainerCollection })));
      const expectedCollection: ICompanyContainer[] = [companyContainer, ...companyContainerCollection];
      jest.spyOn(companyContainerService, 'addCompanyContainerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderContainer });
      comp.ngOnInit();

      expect(companyContainerService.query).toHaveBeenCalled();
      expect(companyContainerService.addCompanyContainerToCollectionIfMissing).toHaveBeenCalledWith(
        companyContainerCollection,
        companyContainer
      );
      expect(comp.companyContainersCollection).toEqual(expectedCollection);
    });

    it('Should call OrderItem query and add missing value', () => {
      const orderContainer: IOrderContainer = { id: 456 };
      const orderItem: IOrderItem = { id: 43237 };
      orderContainer.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 20909 }];
      jest.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      jest.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderContainer });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(orderItemCollection, ...additionalOrderItems);
      expect(comp.orderItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orderContainer: IOrderContainer = { id: 456 };
      const companyContainer: ICompanyContainer = { id: 73981 };
      orderContainer.companyContainer = companyContainer;
      const orderItem: IOrderItem = { id: 31754 };
      orderContainer.orderItem = orderItem;

      activatedRoute.data = of({ orderContainer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(orderContainer));
      expect(comp.companyContainersCollection).toContain(companyContainer);
      expect(comp.orderItemsSharedCollection).toContain(orderItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderContainer>>();
      const orderContainer = { id: 123 };
      jest.spyOn(orderContainerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderContainer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderContainerService.update).toHaveBeenCalledWith(orderContainer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderContainer>>();
      const orderContainer = new OrderContainer();
      jest.spyOn(orderContainerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderContainer }));
      saveSubject.complete();

      // THEN
      expect(orderContainerService.create).toHaveBeenCalledWith(orderContainer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OrderContainer>>();
      const orderContainer = { id: 123 };
      jest.spyOn(orderContainerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderContainerService.update).toHaveBeenCalledWith(orderContainer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCompanyContainerById', () => {
      it('Should return tracked CompanyContainer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompanyContainerById(0, entity);
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
