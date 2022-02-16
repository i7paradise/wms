import { ComponentFixture, TestBed } from '@angular/core/testing';

import { XspsWarehouseComponent } from './xsps-warehouse.component';

describe('XspsWarehouseComponent', () => {
  let component: XspsWarehouseComponent;
  let fixture: ComponentFixture<XspsWarehouseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ XspsWarehouseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(XspsWarehouseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
