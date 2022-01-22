import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DoorAntennaService } from '../service/door-antenna.service';
import { IDoorAntenna, DoorAntenna } from '../door-antenna.model';
import { IDoor } from 'app/entities/door/door.model';
import { DoorService } from 'app/entities/door/service/door.service';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { UHFRFIDAntennaService } from 'app/entities/uhfrfid-antenna/service/uhfrfid-antenna.service';

import { DoorAntennaUpdateComponent } from './door-antenna-update.component';

describe('DoorAntenna Management Update Component', () => {
  let comp: DoorAntennaUpdateComponent;
  let fixture: ComponentFixture<DoorAntennaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let doorAntennaService: DoorAntennaService;
  let doorService: DoorService;
  let uHFRFIDAntennaService: UHFRFIDAntennaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DoorAntennaUpdateComponent],
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
      .overrideTemplate(DoorAntennaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DoorAntennaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    doorAntennaService = TestBed.inject(DoorAntennaService);
    doorService = TestBed.inject(DoorService);
    uHFRFIDAntennaService = TestBed.inject(UHFRFIDAntennaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Door query and add missing value', () => {
      const doorAntenna: IDoorAntenna = { id: 456 };
      const door: IDoor = { id: 66992 };
      doorAntenna.door = door;

      const doorCollection: IDoor[] = [{ id: 73491 }];
      jest.spyOn(doorService, 'query').mockReturnValue(of(new HttpResponse({ body: doorCollection })));
      const additionalDoors = [door];
      const expectedCollection: IDoor[] = [...additionalDoors, ...doorCollection];
      jest.spyOn(doorService, 'addDoorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ doorAntenna });
      comp.ngOnInit();

      expect(doorService.query).toHaveBeenCalled();
      expect(doorService.addDoorToCollectionIfMissing).toHaveBeenCalledWith(doorCollection, ...additionalDoors);
      expect(comp.doorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UHFRFIDAntenna query and add missing value', () => {
      const doorAntenna: IDoorAntenna = { id: 456 };
      const uhfAntenna: IUHFRFIDAntenna = { id: 71539 };
      doorAntenna.uhfAntenna = uhfAntenna;

      const uHFRFIDAntennaCollection: IUHFRFIDAntenna[] = [{ id: 40699 }];
      jest.spyOn(uHFRFIDAntennaService, 'query').mockReturnValue(of(new HttpResponse({ body: uHFRFIDAntennaCollection })));
      const additionalUHFRFIDAntennas = [uhfAntenna];
      const expectedCollection: IUHFRFIDAntenna[] = [...additionalUHFRFIDAntennas, ...uHFRFIDAntennaCollection];
      jest.spyOn(uHFRFIDAntennaService, 'addUHFRFIDAntennaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ doorAntenna });
      comp.ngOnInit();

      expect(uHFRFIDAntennaService.query).toHaveBeenCalled();
      expect(uHFRFIDAntennaService.addUHFRFIDAntennaToCollectionIfMissing).toHaveBeenCalledWith(
        uHFRFIDAntennaCollection,
        ...additionalUHFRFIDAntennas
      );
      expect(comp.uHFRFIDAntennasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const doorAntenna: IDoorAntenna = { id: 456 };
      const door: IDoor = { id: 80049 };
      doorAntenna.door = door;
      const uhfAntenna: IUHFRFIDAntenna = { id: 92854 };
      doorAntenna.uhfAntenna = uhfAntenna;

      activatedRoute.data = of({ doorAntenna });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(doorAntenna));
      expect(comp.doorsSharedCollection).toContain(door);
      expect(comp.uHFRFIDAntennasSharedCollection).toContain(uhfAntenna);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DoorAntenna>>();
      const doorAntenna = { id: 123 };
      jest.spyOn(doorAntennaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ doorAntenna });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: doorAntenna }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(doorAntennaService.update).toHaveBeenCalledWith(doorAntenna);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DoorAntenna>>();
      const doorAntenna = new DoorAntenna();
      jest.spyOn(doorAntennaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ doorAntenna });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: doorAntenna }));
      saveSubject.complete();

      // THEN
      expect(doorAntennaService.create).toHaveBeenCalledWith(doorAntenna);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DoorAntenna>>();
      const doorAntenna = { id: 123 };
      jest.spyOn(doorAntennaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ doorAntenna });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(doorAntennaService.update).toHaveBeenCalledWith(doorAntenna);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDoorById', () => {
      it('Should return tracked Door primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDoorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUHFRFIDAntennaById', () => {
      it('Should return tracked UHFRFIDAntenna primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUHFRFIDAntennaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
