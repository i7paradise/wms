import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceptionItemsComponent } from './reception-items.component';

describe('ReceptionItemsComponent', () => {
  let component: ReceptionItemsComponent;
  let fixture: ComponentFixture<ReceptionItemsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReceptionItemsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReceptionItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
