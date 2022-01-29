import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { UHFRFIDReaderStatus } from 'app/entities/enumerations/uhfrfid-reader-status.model';
import { IUHFRFIDReader, UHFRFIDReader } from '../uhfrfid-reader.model';

import { UHFRFIDReaderService } from './uhfrfid-reader.service';

describe('UHFRFIDReader Service', () => {
  let service: UHFRFIDReaderService;
  let httpMock: HttpTestingController;
  let elemDefault: IUHFRFIDReader;
  let expectedResult: IUHFRFIDReader | IUHFRFIDReader[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UHFRFIDReaderService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      ip: 'AAAAAAA',
      port: 0,
      status: UHFRFIDReaderStatus.DISCONNECTED,
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

    it('should create a UHFRFIDReader', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UHFRFIDReader()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UHFRFIDReader', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          ip: 'BBBBBB',
          port: 1,
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

    it('should partial update a UHFRFIDReader', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          ip: 'BBBBBB',
        },
        new UHFRFIDReader()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UHFRFIDReader', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          ip: 'BBBBBB',
          port: 1,
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

    it('should delete a UHFRFIDReader', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUHFRFIDReaderToCollectionIfMissing', () => {
      it('should add a UHFRFIDReader to an empty array', () => {
        const uHFRFIDReader: IUHFRFIDReader = { id: 123 };
        expectedResult = service.addUHFRFIDReaderToCollectionIfMissing([], uHFRFIDReader);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(uHFRFIDReader);
      });

      it('should not add a UHFRFIDReader to an array that contains it', () => {
        const uHFRFIDReader: IUHFRFIDReader = { id: 123 };
        const uHFRFIDReaderCollection: IUHFRFIDReader[] = [
          {
            ...uHFRFIDReader,
          },
          { id: 456 },
        ];
        expectedResult = service.addUHFRFIDReaderToCollectionIfMissing(uHFRFIDReaderCollection, uHFRFIDReader);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UHFRFIDReader to an array that doesn't contain it", () => {
        const uHFRFIDReader: IUHFRFIDReader = { id: 123 };
        const uHFRFIDReaderCollection: IUHFRFIDReader[] = [{ id: 456 }];
        expectedResult = service.addUHFRFIDReaderToCollectionIfMissing(uHFRFIDReaderCollection, uHFRFIDReader);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uHFRFIDReader);
      });

      it('should add only unique UHFRFIDReader to an array', () => {
        const uHFRFIDReaderArray: IUHFRFIDReader[] = [{ id: 123 }, { id: 456 }, { id: 9161 }];
        const uHFRFIDReaderCollection: IUHFRFIDReader[] = [{ id: 123 }];
        expectedResult = service.addUHFRFIDReaderToCollectionIfMissing(uHFRFIDReaderCollection, ...uHFRFIDReaderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const uHFRFIDReader: IUHFRFIDReader = { id: 123 };
        const uHFRFIDReader2: IUHFRFIDReader = { id: 456 };
        expectedResult = service.addUHFRFIDReaderToCollectionIfMissing([], uHFRFIDReader, uHFRFIDReader2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uHFRFIDReader);
        expect(expectedResult).toContain(uHFRFIDReader2);
      });

      it('should accept null and undefined values', () => {
        const uHFRFIDReader: IUHFRFIDReader = { id: 123 };
        expectedResult = service.addUHFRFIDReaderToCollectionIfMissing([], null, uHFRFIDReader, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(uHFRFIDReader);
      });

      it('should return initial array if no UHFRFIDReader is added', () => {
        const uHFRFIDReaderCollection: IUHFRFIDReader[] = [{ id: 123 }];
        expectedResult = service.addUHFRFIDReaderToCollectionIfMissing(uHFRFIDReaderCollection, undefined, null);
        expect(expectedResult).toEqual(uHFRFIDReaderCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
