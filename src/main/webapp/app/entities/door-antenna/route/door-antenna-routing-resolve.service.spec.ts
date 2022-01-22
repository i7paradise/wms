import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDoorAntenna, DoorAntenna } from '../door-antenna.model';
import { DoorAntennaService } from '../service/door-antenna.service';

import { DoorAntennaRoutingResolveService } from './door-antenna-routing-resolve.service';

describe('DoorAntenna routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DoorAntennaRoutingResolveService;
  let service: DoorAntennaService;
  let resultDoorAntenna: IDoorAntenna | undefined;

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
    routingResolveService = TestBed.inject(DoorAntennaRoutingResolveService);
    service = TestBed.inject(DoorAntennaService);
    resultDoorAntenna = undefined;
  });

  describe('resolve', () => {
    it('should return IDoorAntenna returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDoorAntenna = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDoorAntenna).toEqual({ id: 123 });
    });

    it('should return new IDoorAntenna if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDoorAntenna = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDoorAntenna).toEqual(new DoorAntenna());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DoorAntenna })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDoorAntenna = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDoorAntenna).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
