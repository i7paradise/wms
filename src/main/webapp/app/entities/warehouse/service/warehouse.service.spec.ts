import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWarehouse, Warehouse } from '../warehouse.model';

import { WarehouseService } from './warehouse.service';

describe('Warehouse Service', () => {
  let service: WarehouseService;
  let httpMock: HttpTestingController;
  let elemDefault: IWarehouse;
  let expectedResult: IWarehouse | IWarehouse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WarehouseService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      note: 'AAAAAAA',
      phone: 'AAAAAAA',
      contactPerson: 'AAAAAAA',
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

    it('should create a Warehouse', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Warehouse()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Warehouse', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          note: 'BBBBBB',
          phone: 'BBBBBB',
          contactPerson: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Warehouse', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          phone: 'BBBBBB',
          contactPerson: 'BBBBBB',
        },
        new Warehouse()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Warehouse', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          note: 'BBBBBB',
          phone: 'BBBBBB',
          contactPerson: 'BBBBBB',
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

    it('should delete a Warehouse', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWarehouseToCollectionIfMissing', () => {
      it('should add a Warehouse to an empty array', () => {
        const warehouse: IWarehouse = { id: 123 };
        expectedResult = service.addWarehouseToCollectionIfMissing([], warehouse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warehouse);
      });

      it('should not add a Warehouse to an array that contains it', () => {
        const warehouse: IWarehouse = { id: 123 };
        const warehouseCollection: IWarehouse[] = [
          {
            ...warehouse,
          },
          { id: 456 },
        ];
        expectedResult = service.addWarehouseToCollectionIfMissing(warehouseCollection, warehouse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Warehouse to an array that doesn't contain it", () => {
        const warehouse: IWarehouse = { id: 123 };
        const warehouseCollection: IWarehouse[] = [{ id: 456 }];
        expectedResult = service.addWarehouseToCollectionIfMissing(warehouseCollection, warehouse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warehouse);
      });

      it('should add only unique Warehouse to an array', () => {
        const warehouseArray: IWarehouse[] = [{ id: 123 }, { id: 456 }, { id: 73452 }];
        const warehouseCollection: IWarehouse[] = [{ id: 123 }];
        expectedResult = service.addWarehouseToCollectionIfMissing(warehouseCollection, ...warehouseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const warehouse: IWarehouse = { id: 123 };
        const warehouse2: IWarehouse = { id: 456 };
        expectedResult = service.addWarehouseToCollectionIfMissing([], warehouse, warehouse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warehouse);
        expect(expectedResult).toContain(warehouse2);
      });

      it('should accept null and undefined values', () => {
        const warehouse: IWarehouse = { id: 123 };
        expectedResult = service.addWarehouseToCollectionIfMissing([], null, warehouse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warehouse);
      });

      it('should return initial array if no Warehouse is added', () => {
        const warehouseCollection: IWarehouse[] = [{ id: 123 }];
        expectedResult = service.addWarehouseToCollectionIfMissing(warehouseCollection, undefined, null);
        expect(expectedResult).toEqual(warehouseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
