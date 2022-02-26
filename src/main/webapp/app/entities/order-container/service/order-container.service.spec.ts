import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrderContainer, OrderContainer } from '../order-container.model';

import { OrderContainerService } from './order-container.service';

describe('OrderContainer Service', () => {
  let service: OrderContainerService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrderContainer;
  let expectedResult: IOrderContainer | IOrderContainer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrderContainerService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      supplierRFIDTag: 'AAAAAAA',
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

    it('should create a OrderContainer', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OrderContainer()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderContainer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          supplierRFIDTag: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderContainer', () => {
      const patchObject = Object.assign({}, new OrderContainer());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderContainer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          supplierRFIDTag: 'BBBBBB',
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

    it('should delete a OrderContainer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrderContainerToCollectionIfMissing', () => {
      it('should add a OrderContainer to an empty array', () => {
        const orderContainer: IOrderContainer = { id: 123 };
        expectedResult = service.addOrderContainerToCollectionIfMissing([], orderContainer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderContainer);
      });

      it('should not add a OrderContainer to an array that contains it', () => {
        const orderContainer: IOrderContainer = { id: 123 };
        const orderContainerCollection: IOrderContainer[] = [
          {
            ...orderContainer,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrderContainerToCollectionIfMissing(orderContainerCollection, orderContainer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderContainer to an array that doesn't contain it", () => {
        const orderContainer: IOrderContainer = { id: 123 };
        const orderContainerCollection: IOrderContainer[] = [{ id: 456 }];
        expectedResult = service.addOrderContainerToCollectionIfMissing(orderContainerCollection, orderContainer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderContainer);
      });

      it('should add only unique OrderContainer to an array', () => {
        const orderContainerArray: IOrderContainer[] = [{ id: 123 }, { id: 456 }, { id: 49288 }];
        const orderContainerCollection: IOrderContainer[] = [{ id: 123 }];
        expectedResult = service.addOrderContainerToCollectionIfMissing(orderContainerCollection, ...orderContainerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderContainer: IOrderContainer = { id: 123 };
        const orderContainer2: IOrderContainer = { id: 456 };
        expectedResult = service.addOrderContainerToCollectionIfMissing([], orderContainer, orderContainer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderContainer);
        expect(expectedResult).toContain(orderContainer2);
      });

      it('should accept null and undefined values', () => {
        const orderContainer: IOrderContainer = { id: 123 };
        expectedResult = service.addOrderContainerToCollectionIfMissing([], null, orderContainer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderContainer);
      });

      it('should return initial array if no OrderContainer is added', () => {
        const orderContainerCollection: IOrderContainer[] = [{ id: 123 }];
        expectedResult = service.addOrderContainerToCollectionIfMissing(orderContainerCollection, undefined, null);
        expect(expectedResult).toEqual(orderContainerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
