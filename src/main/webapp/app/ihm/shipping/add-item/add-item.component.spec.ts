import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddItemShippingComponent } from './add-item.component';

describe('AddItemComponent', () => {
  let component: AddItemShippingComponent;
  let fixture: ComponentFixture<AddItemShippingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddItemShippingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddItemShippingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
