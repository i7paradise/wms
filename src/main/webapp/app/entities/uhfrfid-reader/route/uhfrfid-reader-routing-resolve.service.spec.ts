import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IUHFRFIDReader, UHFRFIDReader } from '../uhfrfid-reader.model';
import { UHFRFIDReaderService } from '../service/uhfrfid-reader.service';

import { UHFRFIDReaderRoutingResolveService } from './uhfrfid-reader-routing-resolve.service';

describe('UHFRFIDReader routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: UHFRFIDReaderRoutingResolveService;
  let service: UHFRFIDReaderService;
  let resultUHFRFIDReader: IUHFRFIDReader | undefined;

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
    routingResolveService = TestBed.inject(UHFRFIDReaderRoutingResolveService);
    service = TestBed.inject(UHFRFIDReaderService);
    resultUHFRFIDReader = undefined;
  });

  describe('resolve', () => {
    it('should return IUHFRFIDReader returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUHFRFIDReader = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUHFRFIDReader).toEqual({ id: 123 });
    });

    it('should return new IUHFRFIDReader if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUHFRFIDReader = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUHFRFIDReader).toEqual(new UHFRFIDReader());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UHFRFIDReader })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultUHFRFIDReader = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUHFRFIDReader).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
