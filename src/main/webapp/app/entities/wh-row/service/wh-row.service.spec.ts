import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWHRow, WHRow } from '../wh-row.model';

import { WHRowService } from './wh-row.service';

describe('WHRow Service', () => {
  let service: WHRowService;
  let httpMock: HttpTestingController;
  let elemDefault: IWHRow;
  let expectedResult: IWHRow | IWHRow[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WHRowService);
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

    it('should create a WHRow', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new WHRow()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WHRow', () => {
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

    it('should partial update a WHRow', () => {
      const patchObject = Object.assign(
        {
          note: 'BBBBBB',
        },
        new WHRow()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WHRow', () => {
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

    it('should delete a WHRow', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWHRowToCollectionIfMissing', () => {
      it('should add a WHRow to an empty array', () => {
        const wHRow: IWHRow = { id: 123 };
        expectedResult = service.addWHRowToCollectionIfMissing([], wHRow);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wHRow);
      });

      it('should not add a WHRow to an array that contains it', () => {
        const wHRow: IWHRow = { id: 123 };
        const wHRowCollection: IWHRow[] = [
          {
            ...wHRow,
          },
          { id: 456 },
        ];
        expectedResult = service.addWHRowToCollectionIfMissing(wHRowCollection, wHRow);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WHRow to an array that doesn't contain it", () => {
        const wHRow: IWHRow = { id: 123 };
        const wHRowCollection: IWHRow[] = [{ id: 456 }];
        expectedResult = service.addWHRowToCollectionIfMissing(wHRowCollection, wHRow);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wHRow);
      });

      it('should add only unique WHRow to an array', () => {
        const wHRowArray: IWHRow[] = [{ id: 123 }, { id: 456 }, { id: 51493 }];
        const wHRowCollection: IWHRow[] = [{ id: 123 }];
        expectedResult = service.addWHRowToCollectionIfMissing(wHRowCollection, ...wHRowArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wHRow: IWHRow = { id: 123 };
        const wHRow2: IWHRow = { id: 456 };
        expectedResult = service.addWHRowToCollectionIfMissing([], wHRow, wHRow2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wHRow);
        expect(expectedResult).toContain(wHRow2);
      });

      it('should accept null and undefined values', () => {
        const wHRow: IWHRow = { id: 123 };
        expectedResult = service.addWHRowToCollectionIfMissing([], null, wHRow, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wHRow);
      });

      it('should return initial array if no WHRow is added', () => {
        const wHRowCollection: IWHRow[] = [{ id: 123 }];
        expectedResult = service.addWHRowToCollectionIfMissing(wHRowCollection, undefined, null);
        expect(expectedResult).toEqual(wHRowCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
