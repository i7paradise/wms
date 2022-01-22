import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompanyUser, CompanyUser } from '../company-user.model';

import { CompanyUserService } from './company-user.service';

describe('CompanyUser Service', () => {
  let service: CompanyUserService;
  let httpMock: HttpTestingController;
  let elemDefault: ICompanyUser;
  let expectedResult: ICompanyUser | ICompanyUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompanyUserService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a CompanyUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CompanyUser()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompanyUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompanyUser', () => {
      const patchObject = Object.assign({}, new CompanyUser());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompanyUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a CompanyUser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCompanyUserToCollectionIfMissing', () => {
      it('should add a CompanyUser to an empty array', () => {
        const companyUser: ICompanyUser = { id: 123 };
        expectedResult = service.addCompanyUserToCollectionIfMissing([], companyUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(companyUser);
      });

      it('should not add a CompanyUser to an array that contains it', () => {
        const companyUser: ICompanyUser = { id: 123 };
        const companyUserCollection: ICompanyUser[] = [
          {
            ...companyUser,
          },
          { id: 456 },
        ];
        expectedResult = service.addCompanyUserToCollectionIfMissing(companyUserCollection, companyUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompanyUser to an array that doesn't contain it", () => {
        const companyUser: ICompanyUser = { id: 123 };
        const companyUserCollection: ICompanyUser[] = [{ id: 456 }];
        expectedResult = service.addCompanyUserToCollectionIfMissing(companyUserCollection, companyUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(companyUser);
      });

      it('should add only unique CompanyUser to an array', () => {
        const companyUserArray: ICompanyUser[] = [{ id: 123 }, { id: 456 }, { id: 38727 }];
        const companyUserCollection: ICompanyUser[] = [{ id: 123 }];
        expectedResult = service.addCompanyUserToCollectionIfMissing(companyUserCollection, ...companyUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const companyUser: ICompanyUser = { id: 123 };
        const companyUser2: ICompanyUser = { id: 456 };
        expectedResult = service.addCompanyUserToCollectionIfMissing([], companyUser, companyUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(companyUser);
        expect(expectedResult).toContain(companyUser2);
      });

      it('should accept null and undefined values', () => {
        const companyUser: ICompanyUser = { id: 123 };
        expectedResult = service.addCompanyUserToCollectionIfMissing([], null, companyUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(companyUser);
      });

      it('should return initial array if no CompanyUser is added', () => {
        const companyUserCollection: ICompanyUser[] = [{ id: 123 }];
        expectedResult = service.addCompanyUserToCollectionIfMissing(companyUserCollection, undefined, null);
        expect(expectedResult).toEqual(companyUserCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
