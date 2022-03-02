import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceptionItemDeleteComponent } from './reception-item-delete.component';

describe('ReceptionItemDeleteComponent', () => {
  let component: ReceptionItemDeleteComponent;
  let fixture: ComponentFixture<ReceptionItemDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReceptionItemDeleteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReceptionItemDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
