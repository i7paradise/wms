import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PositionService } from '../service/position.service';
import { IPosition, Position } from '../position.model';
import { IWHLevel } from 'app/entities/wh-level/wh-level.model';
import { WHLevelService } from 'app/entities/wh-level/service/wh-level.service';

import { PositionUpdateComponent } from './position-update.component';

describe('Position Management Update Component', () => {
  let comp: PositionUpdateComponent;
  let fixture: ComponentFixture<PositionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let positionService: PositionService;
  let wHLevelService: WHLevelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PositionUpdateComponent],
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
      .overrideTemplate(PositionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PositionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    positionService = TestBed.inject(PositionService);
    wHLevelService = TestBed.inject(WHLevelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call WHLevel query and add missing value', () => {
      const position: IPosition = { id: 456 };
      const whlevel: IWHLevel = { id: 25153 };
      position.whlevel = whlevel;

      const wHLevelCollection: IWHLevel[] = [{ id: 36062 }];
      jest.spyOn(wHLevelService, 'query').mockReturnValue(of(new HttpResponse({ body: wHLevelCollection })));
      const additionalWHLevels = [whlevel];
      const expectedCollection: IWHLevel[] = [...additionalWHLevels, ...wHLevelCollection];
      jest.spyOn(wHLevelService, 'addWHLevelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ position });
      comp.ngOnInit();

      expect(wHLevelService.query).toHaveBeenCalled();
      expect(wHLevelService.addWHLevelToCollectionIfMissing).toHaveBeenCalledWith(wHLevelCollection, ...additionalWHLevels);
      expect(comp.wHLevelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const position: IPosition = { id: 456 };
      const whlevel: IWHLevel = { id: 59291 };
      position.whlevel = whlevel;

      activatedRoute.data = of({ position });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(position));
      expect(comp.wHLevelsSharedCollection).toContain(whlevel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Position>>();
      const position = { id: 123 };
      jest.spyOn(positionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ position });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: position }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(positionService.update).toHaveBeenCalledWith(position);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Position>>();
      const position = new Position();
      jest.spyOn(positionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ position });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: position }));
      saveSubject.complete();

      // THEN
      expect(positionService.create).toHaveBeenCalledWith(position);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Position>>();
      const position = { id: 123 };
      jest.spyOn(positionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ position });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(positionService.update).toHaveBeenCalledWith(position);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackWHLevelById', () => {
      it('Should return tracked WHLevel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWHLevelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
