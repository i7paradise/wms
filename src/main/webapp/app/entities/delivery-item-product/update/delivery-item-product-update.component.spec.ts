import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeliveryItemProductService } from '../service/delivery-item-product.service';
import { IDeliveryItemProduct, DeliveryItemProduct } from '../delivery-item-product.model';
import { IDeliveryContainer } from 'app/entities/delivery-container/delivery-container.model';
import { DeliveryContainerService } from 'app/entities/delivery-container/service/delivery-container.service';

import { DeliveryItemProductUpdateComponent } from './delivery-item-product-update.component';

describe('DeliveryItemProduct Management Update Component', () => {
  let comp: DeliveryItemProductUpdateComponent;
  let fixture: ComponentFixture<DeliveryItemProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deliveryItemProductService: DeliveryItemProductService;
  let deliveryContainerService: DeliveryContainerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeliveryItemProductUpdateComponent],
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
      .overrideTemplate(DeliveryItemProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliveryItemProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deliveryItemProductService = TestBed.inject(DeliveryItemProductService);
    deliveryContainerService = TestBed.inject(DeliveryContainerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DeliveryContainer query and add missing value', () => {
      const deliveryItemProduct: IDeliveryItemProduct = { id: 456 };
      const deliveryContainer: IDeliveryContainer = { id: 69244 };
      deliveryItemProduct.deliveryContainer = deliveryContainer;

      const deliveryContainerCollection: IDeliveryContainer[] = [{ id: 47296 }];
      jest.spyOn(deliveryContainerService, 'query').mockReturnValue(of(new HttpResponse({ body: deliveryContainerCollection })));
      const additionalDeliveryContainers = [deliveryContainer];
      const expectedCollection: IDeliveryContainer[] = [...additionalDeliveryContainers, ...deliveryContainerCollection];
      jest.spyOn(deliveryContainerService, 'addDeliveryContainerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deliveryItemProduct });
      comp.ngOnInit();

      expect(deliveryContainerService.query).toHaveBeenCalled();
      expect(deliveryContainerService.addDeliveryContainerToCollectionIfMissing).toHaveBeenCalledWith(
        deliveryContainerCollection,
        ...additionalDeliveryContainers
      );
      expect(comp.deliveryContainersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deliveryItemProduct: IDeliveryItemProduct = { id: 456 };
      const deliveryContainer: IDeliveryContainer = { id: 11391 };
      deliveryItemProduct.deliveryContainer = deliveryContainer;

      activatedRoute.data = of({ deliveryItemProduct });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deliveryItemProduct));
      expect(comp.deliveryContainersSharedCollection).toContain(deliveryContainer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryItemProduct>>();
      const deliveryItemProduct = { id: 123 };
      jest.spyOn(deliveryItemProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryItemProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryItemProduct }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deliveryItemProductService.update).toHaveBeenCalledWith(deliveryItemProduct);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryItemProduct>>();
      const deliveryItemProduct = new DeliveryItemProduct();
      jest.spyOn(deliveryItemProductService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryItemProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deliveryItemProduct }));
      saveSubject.complete();

      // THEN
      expect(deliveryItemProductService.create).toHaveBeenCalledWith(deliveryItemProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeliveryItemProduct>>();
      const deliveryItemProduct = { id: 123 };
      jest.spyOn(deliveryItemProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deliveryItemProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deliveryItemProductService.update).toHaveBeenCalledWith(deliveryItemProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDeliveryContainerById', () => {
      it('Should return tracked DeliveryContainer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDeliveryContainerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
