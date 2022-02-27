import { ComponentFixture, TestBed } from '@angular/core/testing';

import { XspsWarehouseDetailComponent } from './xsps-warehouse-detail.component';

describe('XspsWarehouseDetailComponent', () => {
  let component: XspsWarehouseDetailComponent;
  let fixture: ComponentFixture<XspsWarehouseDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ XspsWarehouseDetailComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(XspsWarehouseDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
