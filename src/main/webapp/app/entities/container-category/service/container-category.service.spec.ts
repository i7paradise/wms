import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContainerCategory, ContainerCategory } from '../container-category.model';

import { ContainerCategoryService } from './container-category.service';

describe('ContainerCategory Service', () => {
  let service: ContainerCategoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IContainerCategory;
  let expectedResult: IContainerCategory | IContainerCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContainerCategoryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a ContainerCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ContainerCategory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContainerCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ContainerCategory', () => {
      const patchObject = Object.assign({}, new ContainerCategory());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContainerCategory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a ContainerCategory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContainerCategoryToCollectionIfMissing', () => {
      it('should add a ContainerCategory to an empty array', () => {
        const containerCategory: IContainerCategory = { id: 123 };
        expectedResult = service.addContainerCategoryToCollectionIfMissing([], containerCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(containerCategory);
      });

      it('should not add a ContainerCategory to an array that contains it', () => {
        const containerCategory: IContainerCategory = { id: 123 };
        const containerCategoryCollection: IContainerCategory[] = [
          {
            ...containerCategory,
          },
          { id: 456 },
        ];
        expectedResult = service.addContainerCategoryToCollectionIfMissing(containerCategoryCollection, containerCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContainerCategory to an array that doesn't contain it", () => {
        const containerCategory: IContainerCategory = { id: 123 };
        const containerCategoryCollection: IContainerCategory[] = [{ id: 456 }];
        expectedResult = service.addContainerCategoryToCollectionIfMissing(containerCategoryCollection, containerCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(containerCategory);
      });

      it('should add only unique ContainerCategory to an array', () => {
        const containerCategoryArray: IContainerCategory[] = [{ id: 123 }, { id: 456 }, { id: 57293 }];
        const containerCategoryCollection: IContainerCategory[] = [{ id: 123 }];
        expectedResult = service.addContainerCategoryToCollectionIfMissing(containerCategoryCollection, ...containerCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const containerCategory: IContainerCategory = { id: 123 };
        const containerCategory2: IContainerCategory = { id: 456 };
        expectedResult = service.addContainerCategoryToCollectionIfMissing([], containerCategory, containerCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(containerCategory);
        expect(expectedResult).toContain(containerCategory2);
      });

      it('should accept null and undefined values', () => {
        const containerCategory: IContainerCategory = { id: 123 };
        expectedResult = service.addContainerCategoryToCollectionIfMissing([], null, containerCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(containerCategory);
      });

      it('should return initial array if no ContainerCategory is added', () => {
        const containerCategoryCollection: IContainerCategory[] = [{ id: 123 }];
        expectedResult = service.addContainerCategoryToCollectionIfMissing(containerCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(containerCategoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
