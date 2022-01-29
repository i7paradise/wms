import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICompanyProduct, CompanyProduct } from '../company-product.model';
import { CompanyProductService } from '../service/company-product.service';

import { CompanyProductRoutingResolveService } from './company-product-routing-resolve.service';

describe('CompanyProduct routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CompanyProductRoutingResolveService;
  let service: CompanyProductService;
  let resultCompanyProduct: ICompanyProduct | undefined;

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
    routingResolveService = TestBed.inject(CompanyProductRoutingResolveService);
    service = TestBed.inject(CompanyProductService);
    resultCompanyProduct = undefined;
  });

  describe('resolve', () => {
    it('should return ICompanyProduct returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompanyProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompanyProduct).toEqual({ id: 123 });
    });

    it('should return new ICompanyProduct if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompanyProduct = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCompanyProduct).toEqual(new CompanyProduct());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CompanyProduct })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCompanyProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCompanyProduct).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
