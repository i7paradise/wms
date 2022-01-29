import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeliveryContainerService } from '../service/delivery-container.service';
import { IDeliveryContainer, DeliveryContainer } from '../delivery-container.model';
import { IDeliveryOrderItem } from 'app/entities/delivery-order-item/delivery-order-item.model';
import { DeliveryOrderItemService } from 'app/entities/delivery-order-item/service/delivery-order-item.service';
import { ICompanyContainer } from 'app/entities/company-container/company-container.model';
import { CompanyContainerService } from 'app/entities/company-container/service/company-container.service';

import { DeliveryContainerUpdateComponent } from './delivery-container-update.component';

describe('DeliveryContainer Management Update Component', () => {
  let comp: DeliveryContainerUpdateComponent;
  let fixture: ComponentFixture<DeliveryContainerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deliveryContainerService: DeliveryContainerService;
  let deliveryOrderItemService: DeliveryOrderItemService;
  let companyContainerService: CompanyContainerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeliveryContainerUpdateComponent],
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
      .overrideTemplate(DeliveryContainerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliveryContainerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deliveryContainerService = TestBed.inject(DeliveryContainerService);
    deliveryOrderItemService = TestBed.inject(DeliveryOrderItemService);
    companyContainerService = TestBed.inject(CompanyContainerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DeliveryOrderItem query and add missing value', () => {
      const deliveryContainer: IDeliveryContainer = { id: 456 };
      const deliveryOrderItem: IDeliveryOrderItem = { id: 58112 };
      deliveryContainer.deliveryOrderItem = deliveryOrderItem;

      const deliveryOrderItemCollection: IDeliveryOrderItem[] = [{ id: 81377 }];
      jest.spyOn(deliveryOrderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryOrderItemCollection })));
      const additionalDeliveryOrderItems = [deliveryOrderItem];
      const expectedCollection: IDeliveryOrderItem[] = [...additionalDeliveryOrderItems, ...deliveryOrderItemCollection];
      jest.spyOn(deliveryOrderItemService, 'addDeliveryOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryContainer });
      comp.ngOnInit();

      expect(deliveryOrderItemService.query).toHaveBeenCalled();
      expect(deliveryOrderItemService.addDeliveryOrderItemToCollectionIfMissing).toHaveBeenCalledWith(
        deliveryOrderItemCollection,
        ...additionalDeliveryOrderItems
      );
      expect(comp.deliveryOrderItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CompanyContainer query and add missing value', () => {
      const deliveryContainer: IDeliveryContainer = { id: 456 };
      const companyContainer: ICompanyContainer = { id: 75521 };
      deliveryContainer.companyContainer = companyContainer;

      const companyContainerCollection: ICompanyContainer[] = [{ id: 43082 }];
      jest.spyOn(companyContainerService, 'query').mockReturnValue(of(new HttpResponse({ body: companyContainerCollection })));
      const additionalCompanyContainers = [companyContainer];
      const expectedCollection: ICompanyContainer[] = [...additionalCompanyContainers, ...companyContainerCollection];
      jest.spyOn(companyContainerService, 'addCompanyContainerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryContainer });
      comp.ngOnInit();

      expect(companyContainerService.query).toHaveBeenCalled();
      expect(companyContainerService.addCompanyContainerToCollectionIfMissing).toHaveBeenCalledWith(
        companyContainerCollection,
        ...additionalCompanyContainers
      );
      expect(comp.companyContainersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deliveryContainer: IDeliveryContainer = { id: 456 };
      const deliveryOrderItem: IDeliveryOrderItem = { id: 97325 };
      deliveryContainer.deliveryOrderItem = deliveryOrderItem;
      const companyContainer: ICompanyContainer = { id: 54985 };
      deliveryContainer.companyContainer = companyContainer;

      activatedRoute.data = of({ deliveryContainer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deliveryContainer));
      expect(comp.deliveryOrderItemsSharedCollection).toContain(deliveryOrderItem);
      expect(comp.companyContainersSharedCollection).toContain(companyContainer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryContainer>>();
      const deliveryContainer = { id: 123 };
      jest.spyOn(deliveryContainerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryContainer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deliveryContainerService.update).toHaveBeenCalledWith(deliveryContainer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryContainer>>();
      const deliveryContainer = new DeliveryContainer();
      jest.spyOn(deliveryContainerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryContainer }));
      saveSubject.complete();

      // THEN
      expect(deliveryContainerService.create).toHaveBeenCalledWith(deliveryContainer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryContainer>>();
      const deliveryContainer = { id: 123 };
      jest.spyOn(deliveryContainerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deliveryContainerService.update).toHaveBeenCalledWith(deliveryContainer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDeliveryOrderItemById', () => {
      it('Should return tracked DeliveryOrderItem primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDeliveryOrderItemById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCompanyContainerById', () => {
      it('Should return tracked CompanyContainer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompanyContainerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
