import { TestBed } from '@angular/core/testing';

import { OrderContainerImplService } from './order-container-impl.service';

describe('OrderContainerImplService', () => {
  let service: OrderContainerImplService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderContainerImplService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
