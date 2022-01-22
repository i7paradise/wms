import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DoorService } from '../service/door.service';
import { IDoor, Door } from '../door.model';
import { IArea } from 'app/entities/area/area.model';
import { AreaService } from 'app/entities/area/service/area.service';

import { DoorUpdateComponent } from './door-update.component';

describe('Door Management Update Component', () => {
  let comp: DoorUpdateComponent;
  let fixture: ComponentFixture<DoorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let doorService: DoorService;
  let areaService: AreaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DoorUpdateComponent],
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
      .overrideTemplate(DoorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DoorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    doorService = TestBed.inject(DoorService);
    areaService = TestBed.inject(AreaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Area query and add missing value', () => {
      const door: IDoor = { id: 456 };
      const area: IArea = { id: 21679 };
      door.area = area;

      const areaCollection: IArea[] = [{ id: 29765 }];
      jest.spyOn(areaService, 'query').mockReturnValue(of(new HttpResponse({ body: areaCollection })));
      const additionalAreas = [area];
      const expectedCollection: IArea[] = [...additionalAreas, ...areaCollection];
      jest.spyOn(areaService, 'addAreaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ door });
      comp.ngOnInit();

      expect(areaService.query).toHaveBeenCalled();
      expect(areaService.addAreaToCollectionIfMissing).toHaveBeenCalledWith(areaCollection, ...additionalAreas);
      expect(comp.areasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const door: IDoor = { id: 456 };
      const area: IArea = { id: 55257 };
      door.area = area;

      activatedRoute.data = of({ door });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(door));
      expect(comp.areasSharedCollection).toContain(area);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Door>>();
      const door = { id: 123 };
      jest.spyOn(doorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ door });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: door }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(doorService.update).toHaveBeenCalledWith(door);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Door>>();
      const door = new Door();
      jest.spyOn(doorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ door });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: door }));
      saveSubject.complete();

      // THEN
      expect(doorService.create).toHaveBeenCalledWith(door);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Door>>();
      const door = { id: 123 };
      jest.spyOn(doorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ door });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(doorService.update).toHaveBeenCalledWith(door);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAreaById', () => {
      it('Should return tracked Area primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAreaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
