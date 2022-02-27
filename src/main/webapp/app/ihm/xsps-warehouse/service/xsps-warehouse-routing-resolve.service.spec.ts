import { TestBed } from '@angular/core/testing';

import { XspsWarehouseRoutingResolveService } from './xsps-warehouse-routing-resolve.service';

describe('XspsWarehouseRoutingResolveService', () => {
  let service: XspsWarehouseRoutingResolveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(XspsWarehouseRoutingResolveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
