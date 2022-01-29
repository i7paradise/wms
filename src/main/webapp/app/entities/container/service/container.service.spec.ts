import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContainer, Container } from '../container.model';

import { ContainerService } from './container.service';

describe('Container Service', () => {
  let service: ContainerService;
  let httpMock: HttpTestingController;
  let elemDefault: IContainer;
  let expectedResult: IContainer | IContainer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContainerService);
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

    it('should create a Container', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Container()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Container', () => {
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

    it('should partial update a Container', () => {
      const patchObject = Object.assign({}, new Container());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Container', () => {
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

    it('should delete a Container', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContainerToCollectionIfMissing', () => {
      it('should add a Container to an empty array', () => {
        const container: IContainer = { id: 123 };
        expectedResult = service.addContainerToCollectionIfMissing([], container);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(container);
      });

      it('should not add a Container to an array that contains it', () => {
        const container: IContainer = { id: 123 };
        const containerCollection: IContainer[] = [
          {
            ...container,
          },
          { id: 456 },
        ];
        expectedResult = service.addContainerToCollectionIfMissing(containerCollection, container);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Container to an array that doesn't contain it", () => {
        const container: IContainer = { id: 123 };
        const containerCollection: IContainer[] = [{ id: 456 }];
        expectedResult = service.addContainerToCollectionIfMissing(containerCollection, container);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(container);
      });

      it('should add only unique Container to an array', () => {
        const containerArray: IContainer[] = [{ id: 123 }, { id: 456 }, { id: 87125 }];
        const containerCollection: IContainer[] = [{ id: 123 }];
        expectedResult = service.addContainerToCollectionIfMissing(containerCollection, ...containerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const container: IContainer = { id: 123 };
        const container2: IContainer = { id: 456 };
        expectedResult = service.addContainerToCollectionIfMissing([], container, container2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(container);
        expect(expectedResult).toContain(container2);
      });

      it('should accept null and undefined values', () => {
        const container: IContainer = { id: 123 };
        expectedResult = service.addContainerToCollectionIfMissing([], null, container, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(container);
      });

      it('should return initial array if no Container is added', () => {
        const containerCollection: IContainer[] = [{ id: 123 }];
        expectedResult = service.addContainerToCollectionIfMissing(containerCollection, undefined, null);
        expect(expectedResult).toEqual(containerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
