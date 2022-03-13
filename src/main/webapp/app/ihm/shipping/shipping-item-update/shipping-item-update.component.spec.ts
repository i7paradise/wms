import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShippingItemUpdateComponent } from './shipping-item-update.component';

describe('ShippingItemUpdateComponent', () => {
  let component: ShippingItemUpdateComponent;
  let fixture: ComponentFixture<ShippingItemUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShippingItemUpdateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShippingItemUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
