import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrderItemProduct, OrderItemProduct } from '../order-item-product.model';

import { OrderItemProductService } from './order-item-product.service';

describe('OrderItemProduct Service', () => {
  let service: OrderItemProductService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrderItemProduct;
  let expectedResult: IOrderItemProduct | IOrderItemProduct[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrderItemProductService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      rfidTAG: 'AAAAAAA',
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

    it('should create a OrderItemProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OrderItemProduct()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderItemProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          rfidTAG: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderItemProduct', () => {
      const patchObject = Object.assign({}, new OrderItemProduct());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderItemProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          rfidTAG: 'BBBBBB',
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

    it('should delete a OrderItemProduct', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrderItemProductToCollectionIfMissing', () => {
      it('should add a OrderItemProduct to an empty array', () => {
        const orderItemProduct: IOrderItemProduct = { id: 123 };
        expectedResult = service.addOrderItemProductToCollectionIfMissing([], orderItemProduct);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderItemProduct);
      });

      it('should not add a OrderItemProduct to an array that contains it', () => {
        const orderItemProduct: IOrderItemProduct = { id: 123 };
        const orderItemProductCollection: IOrderItemProduct[] = [
          {
            ...orderItemProduct,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrderItemProductToCollectionIfMissing(orderItemProductCollection, orderItemProduct);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderItemProduct to an array that doesn't contain it", () => {
        const orderItemProduct: IOrderItemProduct = { id: 123 };
        const orderItemProductCollection: IOrderItemProduct[] = [{ id: 456 }];
        expectedResult = service.addOrderItemProductToCollectionIfMissing(orderItemProductCollection, orderItemProduct);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderItemProduct);
      });

      it('should add only unique OrderItemProduct to an array', () => {
        const orderItemProductArray: IOrderItemProduct[] = [{ id: 123 }, { id: 456 }, { id: 46759 }];
        const orderItemProductCollection: IOrderItemProduct[] = [{ id: 123 }];
        expectedResult = service.addOrderItemProductToCollectionIfMissing(orderItemProductCollection, ...orderItemProductArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderItemProduct: IOrderItemProduct = { id: 123 };
        const orderItemProduct2: IOrderItemProduct = { id: 456 };
        expectedResult = service.addOrderItemProductToCollectionIfMissing([], orderItemProduct, orderItemProduct2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderItemProduct);
        expect(expectedResult).toContain(orderItemProduct2);
      });

      it('should accept null and undefined values', () => {
        const orderItemProduct: IOrderItemProduct = { id: 123 };
        expectedResult = service.addOrderItemProductToCollectionIfMissing([], null, orderItemProduct, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderItemProduct);
      });

      it('should return initial array if no OrderItemProduct is added', () => {
        const orderItemProductCollection: IOrderItemProduct[] = [{ id: 123 }];
        expectedResult = service.addOrderItemProductToCollectionIfMissing(orderItemProductCollection, undefined, null);
        expect(expectedResult).toEqual(orderItemProductCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
