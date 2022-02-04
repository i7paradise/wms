import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IContainerCategory, ContainerCategory } from '../container-category.model';
import { ContainerCategoryService } from '../service/container-category.service';

import { ContainerCategoryRoutingResolveService } from './container-category-routing-resolve.service';

describe('ContainerCategory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ContainerCategoryRoutingResolveService;
  let service: ContainerCategoryService;
  let resultContainerCategory: IContainerCategory | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ContainerCategoryRoutingResolveService);
    service = TestBed.inject(ContainerCategoryService);
    resultContainerCategory = undefined;
  });

  describe('resolve', () => {
    it('should return IContainerCategory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultContainerCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultContainerCategory).toEqual({ id: 123 });
    });

    it('should return new IContainerCategory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultContainerCategory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultContainerCategory).toEqual(new ContainerCategory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContainerCategory })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultContainerCategory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultContainerCategory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
