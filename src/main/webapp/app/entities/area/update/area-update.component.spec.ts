import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AreaService } from '../service/area.service';
import { IArea, Area } from '../area.model';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';

import { AreaUpdateComponent } from './area-update.component';

describe('Area Management Update Component', () => {
  let comp: AreaUpdateComponent;
  let fixture: ComponentFixture<AreaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let areaService: AreaService;
  let warehouseService: WarehouseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AreaUpdateComponent],
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
      .overrideTemplate(AreaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AreaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    areaService = TestBed.inject(AreaService);
    warehouseService = TestBed.inject(WarehouseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Warehouse query and add missing value', () => {
      const area: IArea = { id: 456 };
      const warehouse: IWarehouse = { id: 71205 };
      area.warehouse = warehouse;

      const warehouseCollection: IWarehouse[] = [{ id: 21623 }];
      jest.spyOn(warehouseService, 'query').mockReturnValue(of(new HttpResponse({ body: warehouseCollection })));
      const additionalWarehouses = [warehouse];
      const expectedCollection: IWarehouse[] = [...additionalWarehouses, ...warehouseCollection];
      jest.spyOn(warehouseService, 'addWarehouseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ area });
      comp.ngOnInit();

      expect(warehouseService.query).toHaveBeenCalled();
      expect(warehouseService.addWarehouseToCollectionIfMissing).toHaveBeenCalledWith(warehouseCollection, ...additionalWarehouses);
      expect(comp.warehousesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const area: IArea = { id: 456 };
      const warehouse: IWarehouse = { id: 60213 };
      area.warehouse = warehouse;

      activatedRoute.data = of({ area });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(area));
      expect(comp.warehousesSharedCollection).toContain(warehouse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Area>>();
      const area = { id: 123 };
      jest.spyOn(areaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ area });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: area }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(areaService.update).toHaveBeenCalledWith(area);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Area>>();
      const area = new Area();
      jest.spyOn(areaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ area });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: area }));
      saveSubject.complete();

      // THEN
      expect(areaService.create).toHaveBeenCalledWith(area);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Area>>();
      const area = { id: 123 };
      jest.spyOn(areaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ area });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(areaService.update).toHaveBeenCalledWith(area);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackWarehouseById', () => {
      it('Should return tracked Warehouse primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWarehouseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
