import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReceptionService } from './reception.service';
import { DeliveryOrder, IDeliveryOrder } from 'app/entities/delivery-order/delivery-order.model';
import { ReceptionRoutingResolveService } from './reception-routing-resolve.service';

describe('DeliveryOrder routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ReceptionRoutingResolveService;
  let service: ReceptionService;
  let resultDeliveryOrder: IDeliveryOrder | undefined;

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
    routingResolveService = TestBed.inject(ReceptionRoutingResolveService);
    service = TestBed.inject(ReceptionService);
    resultDeliveryOrder = undefined;
  });

  describe('resolve', () => {
    it('should return IDeliveryOrder returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDeliveryOrder = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDeliveryOrder).toEqual({ id: 123 });
    });

    it('should return new IDeliveryOrder if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDeliveryOrder = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDeliveryOrder).toEqual(new DeliveryOrder());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DeliveryOrder })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDeliveryOrder = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDeliveryOrder).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
