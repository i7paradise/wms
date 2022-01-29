import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICompanyContainer, CompanyContainer } from '../company-container.model';
import { CompanyContainerService } from '../service/company-container.service';

import { CompanyContainerRoutingResolveService } from './company-container-routing-resolve.service';

describe('CompanyContainer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CompanyContainerRoutingResolveService;
  let service: CompanyContainerService;
  let resultCompanyContainer: ICompanyContainer | undefined;

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
    routingResolveService = TestBed.inject(CompanyContainerRoutingResolveService);
    service = TestBed.inject(CompanyContainerService);
    resultCompanyContainer = undefined;
  });

  describe('resolve', () => {
    it('should return ICompanyContainer returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompanyContainer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompanyContainer).toEqual({ id: 123 });
    });

    it('should return new ICompanyContainer if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompanyContainer = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCompanyContainer).toEqual(new CompanyContainer());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CompanyContainer })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompanyContainer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompanyContainer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
