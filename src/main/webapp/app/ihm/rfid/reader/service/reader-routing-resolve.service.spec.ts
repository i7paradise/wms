import { TestBed } from '@angular/core/testing';

import { ReaderRoutingResolveService } from './reader-routing-resolve.service';

describe('ReaderRoutingResolveService', () => {
  let service: ReaderRoutingResolveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReaderRoutingResolveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
