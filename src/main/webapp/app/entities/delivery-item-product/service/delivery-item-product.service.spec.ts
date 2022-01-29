import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeliveryItemProduct, DeliveryItemProduct } from '../delivery-item-product.model';

import { DeliveryItemProductService } from './delivery-item-product.service';

describe('DeliveryItemProduct Service', () => {
  let service: DeliveryItemProductService;
  let httpMock: HttpTestingController;
  let elemDefault: IDeliveryItemProduct;
  let expectedResult: IDeliveryItemProduct | IDeliveryItemProduct[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeliveryItemProductService);
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

    it('should create a DeliveryItemProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DeliveryItemProduct()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeliveryItemProduct', () => {
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

    it('should partial update a DeliveryItemProduct', () => {
      const patchObject = Object.assign({}, new DeliveryItemProduct());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeliveryItemProduct', () => {
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

    it('should delete a DeliveryItemProduct', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDeliveryItemProductToCollectionIfMissing', () => {
      it('should add a DeliveryItemProduct to an empty array', () => {
        const deliveryItemProduct: IDeliveryItemProduct = { id: 123 };
        expectedResult = service.addDeliveryItemProductToCollectionIfMissing([], deliveryItemProduct);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryItemProduct);
      });

      it('should not add a DeliveryItemProduct to an array that contains it', () => {
        const deliveryItemProduct: IDeliveryItemProduct = { id: 123 };
        const deliveryItemProductCollection: IDeliveryItemProduct[] = [
          {
            ...deliveryItemProduct,
          },
          { id: 456 },
        ];
        expectedResult = service.addDeliveryItemProductToCollectionIfMissing(deliveryItemProductCollection, deliveryItemProduct);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeliveryItemProduct to an array that doesn't contain it", () => {
        const deliveryItemProduct: IDeliveryItemProduct = { id: 123 };
        const deliveryItemProductCollection: IDeliveryItemProduct[] = [{ id: 456 }];
        expectedResult = service.addDeliveryItemProductToCollectionIfMissing(deliveryItemProductCollection, deliveryItemProduct);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryItemProduct);
      });

      it('should add only unique DeliveryItemProduct to an array', () => {
        const deliveryItemProductArray: IDeliveryItemProduct[] = [{ id: 123 }, { id: 456 }, { id: 95478 }];
        const deliveryItemProductCollection: IDeliveryItemProduct[] = [{ id: 123 }];
        expectedResult = service.addDeliveryItemProductToCollectionIfMissing(deliveryItemProductCollection, ...deliveryItemProductArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deliveryItemProduct: IDeliveryItemProduct = { id: 123 };
        const deliveryItemProduct2: IDeliveryItemProduct = { id: 456 };
        expectedResult = service.addDeliveryItemProductToCollectionIfMissing([], deliveryItemProduct, deliveryItemProduct2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryItemProduct);
        expect(expectedResult).toContain(deliveryItemProduct2);
      });

      it('should accept null and undefined values', () => {
        const deliveryItemProduct: IDeliveryItemProduct = { id: 123 };
        expectedResult = service.addDeliveryItemProductToCollectionIfMissing([], null, deliveryItemProduct, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryItemProduct);
      });

      it('should return initial array if no DeliveryItemProduct is added', () => {
        const deliveryItemProductCollection: IDeliveryItemProduct[] = [{ id: 123 }];
        expectedResult = service.addDeliveryItemProductToCollectionIfMissing(deliveryItemProductCollection, undefined, null);
        expect(expectedResult).toEqual(deliveryItemProductCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
