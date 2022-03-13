import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShippingItemsComponent } from './shipping-items.component';

describe('ShippingItemsComponent', () => {
  let component: ShippingItemsComponent;
  let fixture: ComponentFixture<ShippingItemsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShippingItemsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShippingItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
