import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScanPackagesComponent } from './scan-packages.component';

describe('ScanPackagesComponent', () => {
  let component: ScanPackagesComponent;
  let fixture: ComponentFixture<ScanPackagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScanPackagesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScanPackagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
