import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderContainerEditDialogComponent } from './order-container-edit-dialog.component';

describe('OrderContainerEditDialogComponent', () => {
  let component: OrderContainerEditDialogComponent;
  let fixture: ComponentFixture<OrderContainerEditDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderContainerEditDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderContainerEditDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
