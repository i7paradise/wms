import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderContainerService } from '../service/order-container.service';
import { IOrderContainer, OrderContainer } from '../order-container.model';

import { OrderContainerUpdateComponent } from './order-container-update.component';

describe('OrderContainer Management Update Component', () => {
  let comp: OrderContainerUpdateComponent;
  let fixture: ComponentFixture<OrderContainerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderContainerService: OrderContainerService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const orderContainer: IOrderContainer = { id: 456 };

      activatedRoute.data = of({ orderContainer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(orderContainer));
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
});
