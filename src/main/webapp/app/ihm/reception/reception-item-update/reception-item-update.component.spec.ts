import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceptionItemUpdateComponent } from './reception-item-update.component';

describe('ReceptionItemUpdateComponent', () => {
  let component: ReceptionItemUpdateComponent;
  let fixture: ComponentFixture<ReceptionItemUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReceptionItemUpdateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReceptionItemUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
