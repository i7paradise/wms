import { TestBed } from '@angular/core/testing';

import { ReceptionItemRoutingResolveService } from './reception-item-routing-resolve.service';

describe('ReceptionItemRoutingResolveService', () => {
  let service: ReceptionItemRoutingResolveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReceptionItemRoutingResolveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
