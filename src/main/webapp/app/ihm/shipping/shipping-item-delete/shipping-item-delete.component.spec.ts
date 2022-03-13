import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShippingItemDeleteComponent } from './shipping-item-delete.component';

describe('ShippingItemDeleteComponent', () => {
  let component: ShippingItemDeleteComponent;
  let fixture: ComponentFixture<ShippingItemDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShippingItemDeleteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShippingItemDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
