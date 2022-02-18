import { TestBed } from '@angular/core/testing';

import { XspsWarehouseService } from './xsps-warehouse.service';

describe('XspsWarehouseService', () => {
  let service: XspsWarehouseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(XspsWarehouseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
