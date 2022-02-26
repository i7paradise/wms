import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IOrderItemProduct, OrderItemProduct } from '../order-item-product.model';
import { OrderItemProductService } from '../service/order-item-product.service';

import { OrderItemProductRoutingResolveService } from './order-item-product-routing-resolve.service';

describe('OrderItemProduct routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OrderItemProductRoutingResolveService;
  let service: OrderItemProductService;
  let resultOrderItemProduct: IOrderItemProduct | undefined;

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
    routingResolveService = TestBed.inject(OrderItemProductRoutingResolveService);
    service = TestBed.inject(OrderItemProductService);
    resultOrderItemProduct = undefined;
  });

  describe('resolve', () => {
    it('should return IOrderItemProduct returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderItemProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderItemProduct).toEqual({ id: 123 });
    });

    it('should return new IOrderItemProduct if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderItemProduct = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOrderItemProduct).toEqual(new OrderItemProduct());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrderItemProduct })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrderItemProduct = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrderItemProduct).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
