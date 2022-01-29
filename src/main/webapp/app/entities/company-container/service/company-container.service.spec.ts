import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompanyContainer, CompanyContainer } from '../company-container.model';

import { CompanyContainerService } from './company-container.service';

describe('CompanyContainer Service', () => {
  let service: CompanyContainerService;
  let httpMock: HttpTestingController;
  let elemDefault: ICompanyContainer;
  let expectedResult: ICompanyContainer | ICompanyContainer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompanyContainerService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      rfidTag: 'AAAAAAA',
      color: 'AAAAAAA',
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

    it('should create a CompanyContainer', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CompanyContainer()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompanyContainer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          rfidTag: 'BBBBBB',
          color: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompanyContainer', () => {
      const patchObject = Object.assign(
        {
          color: 'BBBBBB',
        },
        new CompanyContainer()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompanyContainer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          rfidTag: 'BBBBBB',
          color: 'BBBBBB',
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

    it('should delete a CompanyContainer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCompanyContainerToCollectionIfMissing', () => {
      it('should add a CompanyContainer to an empty array', () => {
        const companyContainer: ICompanyContainer = { id: 123 };
        expectedResult = service.addCompanyContainerToCollectionIfMissing([], companyContainer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(companyContainer);
      });

      it('should not add a CompanyContainer to an array that contains it', () => {
        const companyContainer: ICompanyContainer = { id: 123 };
        const companyContainerCollection: ICompanyContainer[] = [
          {
            ...companyContainer,
          },
          { id: 456 },
        ];
        expectedResult = service.addCompanyContainerToCollectionIfMissing(companyContainerCollection, companyContainer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompanyContainer to an array that doesn't contain it", () => {
        const companyContainer: ICompanyContainer = { id: 123 };
        const companyContainerCollection: ICompanyContainer[] = [{ id: 456 }];
        expectedResult = service.addCompanyContainerToCollectionIfMissing(companyContainerCollection, companyContainer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(companyContainer);
      });

      it('should add only unique CompanyContainer to an array', () => {
        const companyContainerArray: ICompanyContainer[] = [{ id: 123 }, { id: 456 }, { id: 50644 }];
        const companyContainerCollection: ICompanyContainer[] = [{ id: 123 }];
        expectedResult = service.addCompanyContainerToCollectionIfMissing(companyContainerCollection, ...companyContainerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const companyContainer: ICompanyContainer = { id: 123 };
        const companyContainer2: ICompanyContainer = { id: 456 };
        expectedResult = service.addCompanyContainerToCollectionIfMissing([], companyContainer, companyContainer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(companyContainer);
        expect(expectedResult).toContain(companyContainer2);
      });

      it('should accept null and undefined values', () => {
        const companyContainer: ICompanyContainer = { id: 123 };
        expectedResult = service.addCompanyContainerToCollectionIfMissing([], null, companyContainer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(companyContainer);
      });

      it('should return initial array if no CompanyContainer is added', () => {
        const companyContainerCollection: ICompanyContainer[] = [{ id: 123 }];
        expectedResult = service.addCompanyContainerToCollectionIfMissing(companyContainerCollection, undefined, null);
        expect(expectedResult).toEqual(companyContainerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
