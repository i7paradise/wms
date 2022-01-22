import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WHLevelService } from '../service/wh-level.service';
import { IWHLevel, WHLevel } from '../wh-level.model';
import { IBay } from 'app/entities/bay/bay.model';
import { BayService } from 'app/entities/bay/service/bay.service';

import { WHLevelUpdateComponent } from './wh-level-update.component';

describe('WHLevel Management Update Component', () => {
  let comp: WHLevelUpdateComponent;
  let fixture: ComponentFixture<WHLevelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wHLevelService: WHLevelService;
  let bayService: BayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WHLevelUpdateComponent],
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
      .overrideTemplate(WHLevelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WHLevelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wHLevelService = TestBed.inject(WHLevelService);
    bayService = TestBed.inject(BayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bay query and add missing value', () => {
      const wHLevel: IWHLevel = { id: 456 };
      const bay: IBay = { id: 30731 };
      wHLevel.bay = bay;

      const bayCollection: IBay[] = [{ id: 30853 }];
      jest.spyOn(bayService, 'query').mockReturnValue(of(new HttpResponse({ body: bayCollection })));
      const additionalBays = [bay];
      const expectedCollection: IBay[] = [...additionalBays, ...bayCollection];
      jest.spyOn(bayService, 'addBayToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wHLevel });
      comp.ngOnInit();

      expect(bayService.query).toHaveBeenCalled();
      expect(bayService.addBayToCollectionIfMissing).toHaveBeenCalledWith(bayCollection, ...additionalBays);
      expect(comp.baysSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const wHLevel: IWHLevel = { id: 456 };
      const bay: IBay = { id: 26184 };
      wHLevel.bay = bay;

      activatedRoute.data = of({ wHLevel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(wHLevel));
      expect(comp.baysSharedCollection).toContain(bay);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WHLevel>>();
      const wHLevel = { id: 123 };
      jest.spyOn(wHLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wHLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wHLevel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(wHLevelService.update).toHaveBeenCalledWith(wHLevel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WHLevel>>();
      const wHLevel = new WHLevel();
      jest.spyOn(wHLevelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wHLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wHLevel }));
      saveSubject.complete();

      // THEN
      expect(wHLevelService.create).toHaveBeenCalledWith(wHLevel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WHLevel>>();
      const wHLevel = { id: 123 };
      jest.spyOn(wHLevelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wHLevel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wHLevelService.update).toHaveBeenCalledWith(wHLevel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBayById', () => {
      it('Should return tracked Bay primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBayById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
