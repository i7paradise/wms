import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UHFRFIDReaderService } from '../service/uhfrfid-reader.service';
import { IUHFRFIDReader, UHFRFIDReader } from '../uhfrfid-reader.model';

import { UHFRFIDReaderUpdateComponent } from './uhfrfid-reader-update.component';

describe('UHFRFIDReader Management Update Component', () => {
  let comp: UHFRFIDReaderUpdateComponent;
  let fixture: ComponentFixture<UHFRFIDReaderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let uHFRFIDReaderService: UHFRFIDReaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UHFRFIDReaderUpdateComponent],
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
      .overrideTemplate(UHFRFIDReaderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UHFRFIDReaderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    uHFRFIDReaderService = TestBed.inject(UHFRFIDReaderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const uHFRFIDReader: IUHFRFIDReader = { id: 456 };

      activatedRoute.data = of({ uHFRFIDReader });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(uHFRFIDReader));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UHFRFIDReader>>();
      const uHFRFIDReader = { id: 123 };
      jest.spyOn(uHFRFIDReaderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uHFRFIDReader });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: uHFRFIDReader }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(uHFRFIDReaderService.update).toHaveBeenCalledWith(uHFRFIDReader);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UHFRFIDReader>>();
      const uHFRFIDReader = new UHFRFIDReader();
      jest.spyOn(uHFRFIDReaderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uHFRFIDReader });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: uHFRFIDReader }));
      saveSubject.complete();

      // THEN
      expect(uHFRFIDReaderService.create).toHaveBeenCalledWith(uHFRFIDReader);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<UHFRFIDReader>>();
      const uHFRFIDReader = { id: 123 };
      jest.spyOn(uHFRFIDReaderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ uHFRFIDReader });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(uHFRFIDReaderService.update).toHaveBeenCalledWith(uHFRFIDReader);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
