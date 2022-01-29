import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBay, Bay } from '../bay.model';

import { BayService } from './bay.service';

describe('Bay Service', () => {
  let service: BayService;
  let httpMock: HttpTestingController;
  let elemDefault: IBay;
  let expectedResult: IBay | IBay[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BayService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      note: 'AAAAAAA',
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

    it('should create a Bay', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Bay()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bay', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          note: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bay', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          note: 'BBBBBB',
        },
        new Bay()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bay', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          note: 'BBBBBB',
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

    it('should delete a Bay', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBayToCollectionIfMissing', () => {
      it('should add a Bay to an empty array', () => {
        const bay: IBay = { id: 123 };
        expectedResult = service.addBayToCollectionIfMissing([], bay);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bay);
      });

      it('should not add a Bay to an array that contains it', () => {
        const bay: IBay = { id: 123 };
        const bayCollection: IBay[] = [
          {
            ...bay,
          },
          { id: 456 },
        ];
        expectedResult = service.addBayToCollectionIfMissing(bayCollection, bay);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bay to an array that doesn't contain it", () => {
        const bay: IBay = { id: 123 };
        const bayCollection: IBay[] = [{ id: 456 }];
        expectedResult = service.addBayToCollectionIfMissing(bayCollection, bay);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bay);
      });

      it('should add only unique Bay to an array', () => {
        const bayArray: IBay[] = [{ id: 123 }, { id: 456 }, { id: 14064 }];
        const bayCollection: IBay[] = [{ id: 123 }];
        expectedResult = service.addBayToCollectionIfMissing(bayCollection, ...bayArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bay: IBay = { id: 123 };
        const bay2: IBay = { id: 456 };
        expectedResult = service.addBayToCollectionIfMissing([], bay, bay2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bay);
        expect(expectedResult).toContain(bay2);
      });

      it('should accept null and undefined values', () => {
        const bay: IBay = { id: 123 };
        expectedResult = service.addBayToCollectionIfMissing([], null, bay, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bay);
      });

      it('should return initial array if no Bay is added', () => {
        const bayCollection: IBay[] = [{ id: 123 }];
        expectedResult = service.addBayToCollectionIfMissing(bayCollection, undefined, null);
        expect(expectedResult).toEqual(bayCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
