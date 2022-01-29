import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeliveryContainer, DeliveryContainer } from '../delivery-container.model';

import { DeliveryContainerService } from './delivery-container.service';

describe('DeliveryContainer Service', () => {
  let service: DeliveryContainerService;
  let httpMock: HttpTestingController;
  let elemDefault: IDeliveryContainer;
  let expectedResult: IDeliveryContainer | IDeliveryContainer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeliveryContainerService);
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

    it('should create a DeliveryContainer', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DeliveryContainer()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeliveryContainer', () => {
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

    it('should partial update a DeliveryContainer', () => {
      const patchObject = Object.assign(
        {
          supplierRFIDTag: 'BBBBBB',
        },
        new DeliveryContainer()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeliveryContainer', () => {
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

    it('should delete a DeliveryContainer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDeliveryContainerToCollectionIfMissing', () => {
      it('should add a DeliveryContainer to an empty array', () => {
        const deliveryContainer: IDeliveryContainer = { id: 123 };
        expectedResult = service.addDeliveryContainerToCollectionIfMissing([], deliveryContainer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryContainer);
      });

      it('should not add a DeliveryContainer to an array that contains it', () => {
        const deliveryContainer: IDeliveryContainer = { id: 123 };
        const deliveryContainerCollection: IDeliveryContainer[] = [
          {
            ...deliveryContainer,
          },
          { id: 456 },
        ];
        expectedResult = service.addDeliveryContainerToCollectionIfMissing(deliveryContainerCollection, deliveryContainer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeliveryContainer to an array that doesn't contain it", () => {
        const deliveryContainer: IDeliveryContainer = { id: 123 };
        const deliveryContainerCollection: IDeliveryContainer[] = [{ id: 456 }];
        expectedResult = service.addDeliveryContainerToCollectionIfMissing(deliveryContainerCollection, deliveryContainer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryContainer);
      });

      it('should add only unique DeliveryContainer to an array', () => {
        const deliveryContainerArray: IDeliveryContainer[] = [{ id: 123 }, { id: 456 }, { id: 5607 }];
        const deliveryContainerCollection: IDeliveryContainer[] = [{ id: 123 }];
        expectedResult = service.addDeliveryContainerToCollectionIfMissing(deliveryContainerCollection, ...deliveryContainerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deliveryContainer: IDeliveryContainer = { id: 123 };
        const deliveryContainer2: IDeliveryContainer = { id: 456 };
        expectedResult = service.addDeliveryContainerToCollectionIfMissing([], deliveryContainer, deliveryContainer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliveryContainer);
        expect(expectedResult).toContain(deliveryContainer2);
      });

      it('should accept null and undefined values', () => {
        const deliveryContainer: IDeliveryContainer = { id: 123 };
        expectedResult = service.addDeliveryContainerToCollectionIfMissing([], null, deliveryContainer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliveryContainer);
      });

      it('should return initial array if no DeliveryContainer is added', () => {
        const deliveryContainerCollection: IDeliveryContainer[] = [{ id: 123 }];
        expectedResult = service.addDeliveryContainerToCollectionIfMissing(deliveryContainerCollection, undefined, null);
        expect(expectedResult).toEqual(deliveryContainerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
