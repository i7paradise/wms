import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DeliveryOrderStatus } from 'app/entities/enumerations/delivery-order-status.model';
import { IDeliveryOrder, DeliveryOrder } from '../delivery-order.model';

import { DeliveryOrderService } from './delivery-order.service';

describe('DeliveryOrder Service', () => {
  let service: DeliveryOrderService;
  let httpMock: HttpTestingController;
  let elemDefault: IDeliveryOrder;
  let expectedResult: IDeliveryOrder | IDeliveryOrder[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeliveryOrderService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      doNumber: 'AAAAAAA',
      placedDate: currentDate,
      status: DeliveryOrderStatus.COMPLETED,
      code: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          placedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DeliveryOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          placedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          placedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new DeliveryOrder()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeliveryOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          doNumber: 'BBBBBB',
          placedDate: currentDate.format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
          code: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          placedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeliveryOrder', () => {
      const patchObject = Object.assign(
        {
          doNumber: 'BBBBBB',
          placedDate: currentDate.format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
        },
        new DeliveryOrder()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          placedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeliveryOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          doNumber: 'BBBBBB',
          placedDate: currentDate.format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
          code: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          placedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DeliveryOrder', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDeliveryOrderToCollectionIfMissing', () => {
      it('should add a DeliveryOrder to an empty array', () => {
        const deliveryOrder: IDeliveryOrder = { id: 123 };
        expectedResult = service.addDeliveryOrderToCollectionIfMissing([], deliveryOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryOrder);
      });

      it('should not add a DeliveryOrder to an array that contains it', () => {
        const deliveryOrder: IDeliveryOrder = { id: 123 };
        const deliveryOrderCollection: IDeliveryOrder[] = [
          {
            ...deliveryOrder,
          },
          { id: 456 },
        ];
        expectedResult = service.addDeliveryOrderToCollectionIfMissing(deliveryOrderCollection, deliveryOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeliveryOrder to an array that doesn't contain it", () => {
        const deliveryOrder: IDeliveryOrder = { id: 123 };
        const deliveryOrderCollection: IDeliveryOrder[] = [{ id: 456 }];
        expectedResult = service.addDeliveryOrderToCollectionIfMissing(deliveryOrderCollection, deliveryOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryOrder);
      });

      it('should add only unique DeliveryOrder to an array', () => {
        const deliveryOrderArray: IDeliveryOrder[] = [{ id: 123 }, { id: 456 }, { id: 11612 }];
        const deliveryOrderCollection: IDeliveryOrder[] = [{ id: 123 }];
        expectedResult = service.addDeliveryOrderToCollectionIfMissing(deliveryOrderCollection, ...deliveryOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deliveryOrder: IDeliveryOrder = { id: 123 };
        const deliveryOrder2: IDeliveryOrder = { id: 456 };
        expectedResult = service.addDeliveryOrderToCollectionIfMissing([], deliveryOrder, deliveryOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryOrder);
        expect(expectedResult).toContain(deliveryOrder2);
      });

      it('should accept null and undefined values', () => {
        const deliveryOrder: IDeliveryOrder = { id: 123 };
        expectedResult = service.addDeliveryOrderToCollectionIfMissing([], null, deliveryOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryOrder);
      });

      it('should return initial array if no DeliveryOrder is added', () => {
        const deliveryOrderCollection: IDeliveryOrder[] = [{ id: 123 }];
        expectedResult = service.addDeliveryOrderToCollectionIfMissing(deliveryOrderCollection, undefined, null);
        expect(expectedResult).toEqual(deliveryOrderCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
