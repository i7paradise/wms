import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { IOrder, Order } from 'app/entities/order/order.model';

import { ReceptionService } from './reception.service';

describe('ReceptionService Service', () => {
  let service: ReceptionService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrder;
  let expectedResult: IOrder | IOrder[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReceptionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      transactionNumber: 'AAAAAAA',
      placedDate: currentDate,
      status: OrderStatus.COMPLETED,
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

    it('should return a list of Order', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          transactionNumber: 'BBBBBB',
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
  });

  afterEach(() => {
    httpMock.verify();
  });
});
