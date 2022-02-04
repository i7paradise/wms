import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContainerCategoryService } from '../service/container-category.service';
import { IContainerCategory, ContainerCategory } from '../container-category.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';

import { ContainerCategoryUpdateComponent } from './container-category-update.component';

describe('ContainerCategory Management Update Component', () => {
  let comp: ContainerCategoryUpdateComponent;
  let fixture: ComponentFixture<ContainerCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let containerCategoryService: ContainerCategoryService;
  let orderItemService: OrderItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContainerCategoryUpdateComponent],
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
      .overrideTemplate(ContainerCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContainerCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    containerCategoryService = TestBed.inject(ContainerCategoryService);
    orderItemService = TestBed.inject(OrderItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call OrderItem query and add missing value', () => {
      const containerCategory: IContainerCategory = { id: 456 };
      const orderItem: IOrderItem = { id: 96301 };
      containerCategory.orderItem = orderItem;

      const orderItemCollection: IOrderItem[] = [{ id: 12447 }];
      jest.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
      const additionalOrderItems = [orderItem];
      const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
      jest.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ containerCategory });
      comp.ngOnInit();

      expect(orderItemService.query).toHaveBeenCalled();
      expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(orderItemCollection, ...additionalOrderItems);
      expect(comp.orderItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const containerCategory: IContainerCategory = { id: 456 };
      const orderItem: IOrderItem = { id: 63065 };
      containerCategory.orderItem = orderItem;

      activatedRoute.data = of({ containerCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(containerCategory));
      expect(comp.orderItemsSharedCollection).toContain(orderItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContainerCategory>>();
      const containerCategory = { id: 123 };
      jest.spyOn(containerCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ containerCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: containerCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(containerCategoryService.update).toHaveBeenCalledWith(containerCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContainerCategory>>();
      const containerCategory = new ContainerCategory();
      jest.spyOn(containerCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ containerCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: containerCategory }));
      saveSubject.complete();

      // THEN
      expect(containerCategoryService.create).toHaveBeenCalledWith(containerCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContainerCategory>>();
      const containerCategory = { id: 123 };
      jest.spyOn(containerCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ containerCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(containerCategoryService.update).toHaveBeenCalledWith(containerCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOrderItemById', () => {
      it('Should return tracked OrderItem primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrderItemById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
