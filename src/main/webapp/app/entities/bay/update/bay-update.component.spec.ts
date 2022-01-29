import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BayService } from '../service/bay.service';
import { IBay, Bay } from '../bay.model';
import { IWHRow } from 'app/entities/wh-row/wh-row.model';
import { WHRowService } from 'app/entities/wh-row/service/wh-row.service';

import { BayUpdateComponent } from './bay-update.component';

describe('Bay Management Update Component', () => {
  let comp: BayUpdateComponent;
  let fixture: ComponentFixture<BayUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bayService: BayService;
  let wHRowService: WHRowService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BayUpdateComponent],
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
      .overrideTemplate(BayUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BayUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bayService = TestBed.inject(BayService);
    wHRowService = TestBed.inject(WHRowService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call WHRow query and add missing value', () => {
      const bay: IBay = { id: 456 };
      const whrow: IWHRow = { id: 60702 };
      bay.whrow = whrow;

      const wHRowCollection: IWHRow[] = [{ id: 28875 }];
      jest.spyOn(wHRowService, 'query').mockReturnValue(of(new HttpResponse({ body: wHRowCollection })));
      const additionalWHRows = [whrow];
      const expectedCollection: IWHRow[] = [...additionalWHRows, ...wHRowCollection];
      jest.spyOn(wHRowService, 'addWHRowToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bay });
      comp.ngOnInit();

      expect(wHRowService.query).toHaveBeenCalled();
      expect(wHRowService.addWHRowToCollectionIfMissing).toHaveBeenCalledWith(wHRowCollection, ...additionalWHRows);
      expect(comp.wHRowsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bay: IBay = { id: 456 };
      const whrow: IWHRow = { id: 47062 };
      bay.whrow = whrow;

      activatedRoute.data = of({ bay });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bay));
      expect(comp.wHRowsSharedCollection).toContain(whrow);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bay>>();
      const bay = { id: 123 };
      jest.spyOn(bayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bay });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bay }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bayService.update).toHaveBeenCalledWith(bay);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bay>>();
      const bay = new Bay();
      jest.spyOn(bayService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bay });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bay }));
      saveSubject.complete();

      // THEN
      expect(bayService.create).toHaveBeenCalledWith(bay);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bay>>();
      const bay = { id: 123 };
      jest.spyOn(bayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bay });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bayService.update).toHaveBeenCalledWith(bay);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackWHRowById', () => {
      it('Should return tracked WHRow primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWHRowById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
