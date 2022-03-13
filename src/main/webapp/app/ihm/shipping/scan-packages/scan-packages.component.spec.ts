import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScanPackagesShippingComponent } from './scan-packages.component';

describe('ScanPackagesComponent', () => {
  let component: ScanPackagesShippingComponent;
  let fixture: ComponentFixture<ScanPackagesShippingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScanPackagesShippingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScanPackagesShippingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
