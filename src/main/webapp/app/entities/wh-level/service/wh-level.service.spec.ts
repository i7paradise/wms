import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWHLevel, WHLevel } from '../wh-level.model';

import { WHLevelService } from './wh-level.service';

describe('WHLevel Service', () => {
  let service: WHLevelService;
  let httpMock: HttpTestingController;
  let elemDefault: IWHLevel;
  let expectedResult: IWHLevel | IWHLevel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WHLevelService);
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

    it('should create a WHLevel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new WHLevel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WHLevel', () => {
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

    it('should partial update a WHLevel', () => {
      const patchObject = Object.assign(
        {
          note: 'BBBBBB',
        },
        new WHLevel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WHLevel', () => {
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

    it('should delete a WHLevel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWHLevelToCollectionIfMissing', () => {
      it('should add a WHLevel to an empty array', () => {
        const wHLevel: IWHLevel = { id: 123 };
        expectedResult = service.addWHLevelToCollectionIfMissing([], wHLevel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wHLevel);
      });

      it('should not add a WHLevel to an array that contains it', () => {
        const wHLevel: IWHLevel = { id: 123 };
        const wHLevelCollection: IWHLevel[] = [
          {
            ...wHLevel,
          },
          { id: 456 },
        ];
        expectedResult = service.addWHLevelToCollectionIfMissing(wHLevelCollection, wHLevel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WHLevel to an array that doesn't contain it", () => {
        const wHLevel: IWHLevel = { id: 123 };
        const wHLevelCollection: IWHLevel[] = [{ id: 456 }];
        expectedResult = service.addWHLevelToCollectionIfMissing(wHLevelCollection, wHLevel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wHLevel);
      });

      it('should add only unique WHLevel to an array', () => {
        const wHLevelArray: IWHLevel[] = [{ id: 123 }, { id: 456 }, { id: 83993 }];
        const wHLevelCollection: IWHLevel[] = [{ id: 123 }];
        expectedResult = service.addWHLevelToCollectionIfMissing(wHLevelCollection, ...wHLevelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wHLevel: IWHLevel = { id: 123 };
        const wHLevel2: IWHLevel = { id: 456 };
        expectedResult = service.addWHLevelToCollectionIfMissing([], wHLevel, wHLevel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wHLevel);
        expect(expectedResult).toContain(wHLevel2);
      });

      it('should accept null and undefined values', () => {
        const wHLevel: IWHLevel = { id: 123 };
        expectedResult = service.addWHLevelToCollectionIfMissing([], null, wHLevel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wHLevel);
      });

      it('should return initial array if no WHLevel is added', () => {
        const wHLevelCollection: IWHLevel[] = [{ id: 123 }];
        expectedResult = service.addWHLevelToCollectionIfMissing(wHLevelCollection, undefined, null);
        expect(expectedResult).toEqual(wHLevelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
