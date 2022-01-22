import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDoor, Door } from '../door.model';

import { DoorService } from './door.service';

describe('Door Service', () => {
  let service: DoorService;
  let httpMock: HttpTestingController;
  let elemDefault: IDoor;
  let expectedResult: IDoor | IDoor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DoorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a Door', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Door()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Door', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Door', () => {
      const patchObject = Object.assign({}, new Door());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Door', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a Door', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDoorToCollectionIfMissing', () => {
      it('should add a Door to an empty array', () => {
        const door: IDoor = { id: 123 };
        expectedResult = service.addDoorToCollectionIfMissing([], door);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(door);
      });

      it('should not add a Door to an array that contains it', () => {
        const door: IDoor = { id: 123 };
        const doorCollection: IDoor[] = [
          {
            ...door,
          },
          { id: 456 },
        ];
        expectedResult = service.addDoorToCollectionIfMissing(doorCollection, door);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Door to an array that doesn't contain it", () => {
        const door: IDoor = { id: 123 };
        const doorCollection: IDoor[] = [{ id: 456 }];
        expectedResult = service.addDoorToCollectionIfMissing(doorCollection, door);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(door);
      });

      it('should add only unique Door to an array', () => {
        const doorArray: IDoor[] = [{ id: 123 }, { id: 456 }, { id: 18106 }];
        const doorCollection: IDoor[] = [{ id: 123 }];
        expectedResult = service.addDoorToCollectionIfMissing(doorCollection, ...doorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const door: IDoor = { id: 123 };
        const door2: IDoor = { id: 456 };
        expectedResult = service.addDoorToCollectionIfMissing([], door, door2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(door);
        expect(expectedResult).toContain(door2);
      });

      it('should accept null and undefined values', () => {
        const door: IDoor = { id: 123 };
        expectedResult = service.addDoorToCollectionIfMissing([], null, door, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(door);
      });

      it('should return initial array if no Door is added', () => {
        const doorCollection: IDoor[] = [{ id: 123 }];
        expectedResult = service.addDoorToCollectionIfMissing(doorCollection, undefined, null);
        expect(expectedResult).toEqual(doorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
