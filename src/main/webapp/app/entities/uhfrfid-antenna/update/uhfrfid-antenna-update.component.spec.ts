import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UHFRFIDAntennaService } from '../service/uhfrfid-antenna.service';
import { IUHFRFIDAntenna, UHFRFIDAntenna } from '../uhfrfid-antenna.model';
import { IUHFRFIDReader } from 'app/entities/uhfrfid-reader/uhfrfid-reader.model';
import { UHFRFIDReaderService } from 'app/entities/uhfrfid-reader/service/uhfrfid-reader.service';

import { UHFRFIDAntennaUpdateComponent } from './uhfrfid-antenna-update.component';

describe('UHFRFIDAntenna Management Update Component', () => {
  let comp: UHFRFIDAntennaUpdateComponent;
  let fixture: ComponentFixture<UHFRFIDAntennaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let uHFRFIDAntennaService: UHFRFIDAntennaService;
  let uHFRFIDReaderService: UHFRFIDReaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UHFRFIDAntennaUpdateComponent],
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
      .overrideTemplate(UHFRFIDAntennaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UHFRFIDAntennaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    uHFRFIDAntennaService = TestBed.inject(UHFRFIDAntennaService);
    uHFRFIDReaderService = TestBed.inject(UHFRFIDReaderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UHFRFIDReader query and add missing value', () => {
      const uHFRFIDAntenna: IUHFRFIDAntenna = { id: 456 };
      const uhfReader: IUHFRFIDReader = { id: 97718 };
      uHFRFIDAntenna.uhfReader = uhfReader;

      const uHFRFIDReaderCollection: IUHFRFIDReader[] = [{ id: 9797 }];
      jest.spyOn(uHFRFIDReaderService, 'query').mockReturnValue(of(new HttpResponse({ body: uHFRFIDReaderCollection })));
      const additionalUHFRFIDReaders = [uhfReader];
      const expectedCollection: IUHFRFIDReader[] = [...additionalUHFRFIDReaders, ...uHFRFIDReaderCollection];
      jest.spyOn(uHFRFIDReaderService, 'addUHFRFIDReaderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ uHFRFIDAntenna });
      comp.ngOnInit();

      expect(uHFRFIDReaderService.query).toHaveBeenCalled();
      expect(uHFRFIDReaderService.addUHFRFIDReaderToCollectionIfMissing).toHaveBeenCalledWith(
        uHFRFIDReaderCollection,
        ...additionalUHFRFIDReaders
      );
      expect(comp.uHFRFIDReadersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const uHFRFIDAntenna: IUHFRFIDAntenna = { id: 456 };
      const uhfReader: IUHFRFIDReader = { id: 44487 };
      uHFRFIDAntenna.uhfReader = uhfReader;

      activatedRoute.data = of({ uHFRFIDAntenna });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(uHFRFIDAntenna));
      expect(comp.uHFRFIDReadersSharedCollection).toContain(uhfReader);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UHFRFIDAntenna>>();
      const uHFRFIDAntenna = { id: 123 };
      jest.spyOn(uHFRFIDAntennaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uHFRFIDAntenna });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: uHFRFIDAntenna }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(uHFRFIDAntennaService.update).toHaveBeenCalledWith(uHFRFIDAntenna);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UHFRFIDAntenna>>();
      const uHFRFIDAntenna = new UHFRFIDAntenna();
      jest.spyOn(uHFRFIDAntennaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uHFRFIDAntenna });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: uHFRFIDAntenna }));
      saveSubject.complete();

      // THEN
      expect(uHFRFIDAntennaService.create).toHaveBeenCalledWith(uHFRFIDAntenna);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UHFRFIDAntenna>>();
      const uHFRFIDAntenna = { id: 123 };
      jest.spyOn(uHFRFIDAntennaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uHFRFIDAntenna });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(uHFRFIDAntennaService.update).toHaveBeenCalledWith(uHFRFIDAntenna);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUHFRFIDReaderById', () => {
      it('Should return tracked UHFRFIDReader primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUHFRFIDReaderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
