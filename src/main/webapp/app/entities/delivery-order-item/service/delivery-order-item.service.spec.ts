import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DeliveryOrderItemStatus } from 'app/entities/enumerations/delivery-order-item-status.model';
import { IDeliveryOrderItem, DeliveryOrderItem } from '../delivery-order-item.model';

import { DeliveryOrderItemService } from './delivery-order-item.service';

describe('DeliveryOrderItem Service', () => {
  let service: DeliveryOrderItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IDeliveryOrderItem;
  let expectedResult: IDeliveryOrderItem | IDeliveryOrderItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeliveryOrderItemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      unitQuantity: 0,
      containerQuantity: 0,
      status: DeliveryOrderItemStatus.IN_PROGRESS,
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

    it('should create a DeliveryOrderItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DeliveryOrderItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeliveryOrderItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          unitQuantity: 1,
          containerQuantity: 1,
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

    it('should partial update a DeliveryOrderItem', () => {
      const patchObject = Object.assign(
        {
          unitQuantity: 1,
          status: 'BBBBBB',
        },
        new DeliveryOrderItem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeliveryOrderItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          unitQuantity: 1,
          containerQuantity: 1,
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

    it('should delete a DeliveryOrderItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDeliveryOrderItemToCollectionIfMissing', () => {
      it('should add a DeliveryOrderItem to an empty array', () => {
        const deliveryOrderItem: IDeliveryOrderItem = { id: 123 };
        expectedResult = service.addDeliveryOrderItemToCollectionIfMissing([], deliveryOrderItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryOrderItem);
      });

      it('should not add a DeliveryOrderItem to an array that contains it', () => {
        const deliveryOrderItem: IDeliveryOrderItem = { id: 123 };
        const deliveryOrderItemCollection: IDeliveryOrderItem[] = [
          {
            ...deliveryOrderItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addDeliveryOrderItemToCollectionIfMissing(deliveryOrderItemCollection, deliveryOrderItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeliveryOrderItem to an array that doesn't contain it", () => {
        const deliveryOrderItem: IDeliveryOrderItem = { id: 123 };
        const deliveryOrderItemCollection: IDeliveryOrderItem[] = [{ id: 456 }];
        expectedResult = service.addDeliveryOrderItemToCollectionIfMissing(deliveryOrderItemCollection, deliveryOrderItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryOrderItem);
      });

      it('should add only unique DeliveryOrderItem to an array', () => {
        const deliveryOrderItemArray: IDeliveryOrderItem[] = [{ id: 123 }, { id: 456 }, { id: 59262 }];
        const deliveryOrderItemCollection: IDeliveryOrderItem[] = [{ id: 123 }];
        expectedResult = service.addDeliveryOrderItemToCollectionIfMissing(deliveryOrderItemCollection, ...deliveryOrderItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deliveryOrderItem: IDeliveryOrderItem = { id: 123 };
        const deliveryOrderItem2: IDeliveryOrderItem = { id: 456 };
        expectedResult = service.addDeliveryOrderItemToCollectionIfMissing([], deliveryOrderItem, deliveryOrderItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryOrderItem);
        expect(expectedResult).toContain(deliveryOrderItem2);
      });

      it('should accept null and undefined values', () => {
        const deliveryOrderItem: IDeliveryOrderItem = { id: 123 };
        expectedResult = service.addDeliveryOrderItemToCollectionIfMissing([], null, deliveryOrderItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryOrderItem);
      });

      it('should return initial array if no DeliveryOrderItem is added', () => {
        const deliveryOrderItemCollection: IDeliveryOrderItem[] = [{ id: 123 }];
        expectedResult = service.addDeliveryOrderItemToCollectionIfMissing(deliveryOrderItemCollection, undefined, null);
        expect(expectedResult).toEqual(deliveryOrderItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
