import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShippingTagsComponent } from './shipping-tags.component';

describe('ShippingTagsComponent', () => {
  let component: ShippingTagsComponent;
  let fixture: ComponentFixture<ShippingTagsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShippingTagsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShippingTagsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
