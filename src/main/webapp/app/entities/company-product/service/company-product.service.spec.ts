import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompanyProduct, CompanyProduct } from '../company-product.model';

import { CompanyProductService } from './company-product.service';

describe('CompanyProduct Service', () => {
  let service: CompanyProductService;
  let httpMock: HttpTestingController;
  let elemDefault: ICompanyProduct;
  let expectedResult: ICompanyProduct | ICompanyProduct[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompanyProductService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      quantity: 0,
      sku: 'AAAAAAA',
      containerStockingRatio: 0,
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

    it('should create a CompanyProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CompanyProduct()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompanyProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantity: 1,
          sku: 'BBBBBB',
          containerStockingRatio: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompanyProduct', () => {
      const patchObject = Object.assign(
        {
          quantity: 1,
          sku: 'BBBBBB',
        },
        new CompanyProduct()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompanyProduct', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantity: 1,
          sku: 'BBBBBB',
          containerStockingRatio: 1,
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

    it('should delete a CompanyProduct', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCompanyProductToCollectionIfMissing', () => {
      it('should add a CompanyProduct to an empty array', () => {
        const companyProduct: ICompanyProduct = { id: 123 };
        expectedResult = service.addCompanyProductToCollectionIfMissing([], companyProduct);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(companyProduct);
      });

      it('should not add a CompanyProduct to an array that contains it', () => {
        const companyProduct: ICompanyProduct = { id: 123 };
        const companyProductCollection: ICompanyProduct[] = [
          {
            ...companyProduct,
          },
          { id: 456 },
        ];
        expectedResult = service.addCompanyProductToCollectionIfMissing(companyProductCollection, companyProduct);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompanyProduct to an array that doesn't contain it", () => {
        const companyProduct: ICompanyProduct = { id: 123 };
        const companyProductCollection: ICompanyProduct[] = [{ id: 456 }];
        expectedResult = service.addCompanyProductToCollectionIfMissing(companyProductCollection, companyProduct);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(companyProduct);
      });

      it('should add only unique CompanyProduct to an array', () => {
        const companyProductArray: ICompanyProduct[] = [{ id: 123 }, { id: 456 }, { id: 13511 }];
        const companyProductCollection: ICompanyProduct[] = [{ id: 123 }];
        expectedResult = service.addCompanyProductToCollectionIfMissing(companyProductCollection, ...companyProductArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const companyProduct: ICompanyProduct = { id: 123 };
        const companyProduct2: ICompanyProduct = { id: 456 };
        expectedResult = service.addCompanyProductToCollectionIfMissing([], companyProduct, companyProduct2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(companyProduct);
        expect(expectedResult).toContain(companyProduct2);
      });

      it('should accept null and undefined values', () => {
        const companyProduct: ICompanyProduct = { id: 123 };
        expectedResult = service.addCompanyProductToCollectionIfMissing([], null, companyProduct, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(companyProduct);
      });

      it('should return initial array if no CompanyProduct is added', () => {
        const companyProductCollection: ICompanyProduct[] = [{ id: 123 }];
        expectedResult = service.addCompanyProductToCollectionIfMissing(companyProductCollection, undefined, null);
        expect(expectedResult).toEqual(companyProductCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
