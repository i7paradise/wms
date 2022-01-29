import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { UHFRFIDAntennaStatus } from 'app/entities/enumerations/uhfrfid-antenna-status.model';
import { IUHFRFIDAntenna, UHFRFIDAntenna } from '../uhfrfid-antenna.model';

import { UHFRFIDAntennaService } from './uhfrfid-antenna.service';

describe('UHFRFIDAntenna Service', () => {
  let service: UHFRFIDAntennaService;
  let httpMock: HttpTestingController;
  let elemDefault: IUHFRFIDAntenna;
  let expectedResult: IUHFRFIDAntenna | IUHFRFIDAntenna[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UHFRFIDAntennaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      outputPower: 0,
      status: UHFRFIDAntennaStatus.AVAILABLE,
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

    it('should create a UHFRFIDAntenna', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UHFRFIDAntenna()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UHFRFIDAntenna', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          outputPower: 1,
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UHFRFIDAntenna', () => {
      const patchObject = Object.assign(
        {
          outputPower: 1,
          status: 'BBBBBB',
        },
        new UHFRFIDAntenna()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UHFRFIDAntenna', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          outputPower: 1,
          status: 'BBBBBB',
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

    it('should delete a UHFRFIDAntenna', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUHFRFIDAntennaToCollectionIfMissing', () => {
      it('should add a UHFRFIDAntenna to an empty array', () => {
        const uHFRFIDAntenna: IUHFRFIDAntenna = { id: 123 };
        expectedResult = service.addUHFRFIDAntennaToCollectionIfMissing([], uHFRFIDAntenna);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(uHFRFIDAntenna);
      });

      it('should not add a UHFRFIDAntenna to an array that contains it', () => {
        const uHFRFIDAntenna: IUHFRFIDAntenna = { id: 123 };
        const uHFRFIDAntennaCollection: IUHFRFIDAntenna[] = [
          {
            ...uHFRFIDAntenna,
          },
          { id: 456 },
        ];
        expectedResult = service.addUHFRFIDAntennaToCollectionIfMissing(uHFRFIDAntennaCollection, uHFRFIDAntenna);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UHFRFIDAntenna to an array that doesn't contain it", () => {
        const uHFRFIDAntenna: IUHFRFIDAntenna = { id: 123 };
        const uHFRFIDAntennaCollection: IUHFRFIDAntenna[] = [{ id: 456 }];
        expectedResult = service.addUHFRFIDAntennaToCollectionIfMissing(uHFRFIDAntennaCollection, uHFRFIDAntenna);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uHFRFIDAntenna);
      });

      it('should add only unique UHFRFIDAntenna to an array', () => {
        const uHFRFIDAntennaArray: IUHFRFIDAntenna[] = [{ id: 123 }, { id: 456 }, { id: 8021 }];
        const uHFRFIDAntennaCollection: IUHFRFIDAntenna[] = [{ id: 123 }];
        expectedResult = service.addUHFRFIDAntennaToCollectionIfMissing(uHFRFIDAntennaCollection, ...uHFRFIDAntennaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const uHFRFIDAntenna: IUHFRFIDAntenna = { id: 123 };
        const uHFRFIDAntenna2: IUHFRFIDAntenna = { id: 456 };
        expectedResult = service.addUHFRFIDAntennaToCollectionIfMissing([], uHFRFIDAntenna, uHFRFIDAntenna2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uHFRFIDAntenna);
        expect(expectedResult).toContain(uHFRFIDAntenna2);
      });

      it('should accept null and undefined values', () => {
        const uHFRFIDAntenna: IUHFRFIDAntenna = { id: 123 };
        expectedResult = service.addUHFRFIDAntennaToCollectionIfMissing([], null, uHFRFIDAntenna, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(uHFRFIDAntenna);
      });

      it('should return initial array if no UHFRFIDAntenna is added', () => {
        const uHFRFIDAntennaCollection: IUHFRFIDAntenna[] = [{ id: 123 }];
        expectedResult = service.addUHFRFIDAntennaToCollectionIfMissing(uHFRFIDAntennaCollection, undefined, null);
        expect(expectedResult).toEqual(uHFRFIDAntennaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
