import { TestBed } from '@angular/core/testing';

import { ShippingItemRoutingResolveService } from './shipping-item-routing-resolve.service';

describe('ShippingItemRoutingResolveService', () => {
  let service: ShippingItemRoutingResolveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShippingItemRoutingResolveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
