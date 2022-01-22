import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DoorAntennaType } from 'app/entities/enumerations/door-antenna-type.model';
import { IDoorAntenna, DoorAntenna } from '../door-antenna.model';

import { DoorAntennaService } from './door-antenna.service';

describe('DoorAntenna Service', () => {
  let service: DoorAntennaService;
  let httpMock: HttpTestingController;
  let elemDefault: IDoorAntenna;
  let expectedResult: IDoorAntenna | IDoorAntenna[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DoorAntennaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      type: DoorAntennaType.INNER,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DoorAntenna', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DoorAntenna()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DoorAntenna', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          type: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DoorAntenna', () => {
      const patchObject = Object.assign({}, new DoorAntenna());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DoorAntenna', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          type: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DoorAntenna', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDoorAntennaToCollectionIfMissing', () => {
      it('should add a DoorAntenna to an empty array', () => {
        const doorAntenna: IDoorAntenna = { id: 123 };
        expectedResult = service.addDoorAntennaToCollectionIfMissing([], doorAntenna);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(doorAntenna);
      });

      it('should not add a DoorAntenna to an array that contains it', () => {
        const doorAntenna: IDoorAntenna = { id: 123 };
        const doorAntennaCollection: IDoorAntenna[] = [
          {
            ...doorAntenna,
          },
          { id: 456 },
        ];
        expectedResult = service.addDoorAntennaToCollectionIfMissing(doorAntennaCollection, doorAntenna);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DoorAntenna to an array that doesn't contain it", () => {
        const doorAntenna: IDoorAntenna = { id: 123 };
        const doorAntennaCollection: IDoorAntenna[] = [{ id: 456 }];
        expectedResult = service.addDoorAntennaToCollectionIfMissing(doorAntennaCollection, doorAntenna);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(doorAntenna);
      });

      it('should add only unique DoorAntenna to an array', () => {
        const doorAntennaArray: IDoorAntenna[] = [{ id: 123 }, { id: 456 }, { id: 52499 }];
        const doorAntennaCollection: IDoorAntenna[] = [{ id: 123 }];
        expectedResult = service.addDoorAntennaToCollectionIfMissing(doorAntennaCollection, ...doorAntennaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const doorAntenna: IDoorAntenna = { id: 123 };
        const doorAntenna2: IDoorAntenna = { id: 456 };
        expectedResult = service.addDoorAntennaToCollectionIfMissing([], doorAntenna, doorAntenna2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(doorAntenna);
        expect(expectedResult).toContain(doorAntenna2);
      });

      it('should accept null and undefined values', () => {
        const doorAntenna: IDoorAntenna = { id: 123 };
        expectedResult = service.addDoorAntennaToCollectionIfMissing([], null, doorAntenna, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(doorAntenna);
      });

      it('should return initial array if no DoorAntenna is added', () => {
        const doorAntennaCollection: IDoorAntenna[] = [{ id: 123 }];
        expectedResult = service.addDoorAntennaToCollectionIfMissing(doorAntennaCollection, undefined, null);
        expect(expectedResult).toEqual(doorAntennaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
