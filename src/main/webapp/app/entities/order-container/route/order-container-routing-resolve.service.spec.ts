import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IOrderContainer, OrderContainer } from '../order-container.model';
import { OrderContainerService } from '../service/order-container.service';

import { OrderContainerRoutingResolveService } from './order-container-routing-resolve.service';

describe('OrderContainer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OrderContainerRoutingResolveService;
  let service: OrderContainerService;
  let resultOrderContainer: IOrderContainer | undefined;

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
    routingResolveService = TestBed.inject(OrderContainerRoutingResolveService);
    service = TestBed.inject(OrderContainerService);
    resultOrderContainer = undefined;
  });

  describe('resolve', () => {
    it('should return IOrderContainer returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderContainer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderContainer).toEqual({ id: 123 });
    });

    it('should return new IOrderContainer if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderContainer = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOrderContainer).toEqual(new OrderContainer());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrderContainer })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderContainer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderContainer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
