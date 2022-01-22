import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDeliveryOrderItem, DeliveryOrderItem } from '../delivery-order-item.model';
import { DeliveryOrderItemService } from '../service/delivery-order-item.service';

import { DeliveryOrderItemRoutingResolveService } from './delivery-order-item-routing-resolve.service';

describe('DeliveryOrderItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DeliveryOrderItemRoutingResolveService;
  let service: DeliveryOrderItemService;
  let resultDeliveryOrderItem: IDeliveryOrderItem | undefined;

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
    routingResolveService = TestBed.inject(DeliveryOrderItemRoutingResolveService);
    service = TestBed.inject(DeliveryOrderItemService);
    resultDeliveryOrderItem = undefined;
  });

  describe('resolve', () => {
    it('should return IDeliveryOrderItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDeliveryOrderItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDeliveryOrderItem).toEqual({ id: 123 });
    });

    it('should return new IDeliveryOrderItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDeliveryOrderItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDeliveryOrderItem).toEqual(new DeliveryOrderItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DeliveryOrderItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDeliveryOrderItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDeliveryOrderItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
