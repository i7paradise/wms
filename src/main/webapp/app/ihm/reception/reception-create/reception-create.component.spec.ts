import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceptionCreateComponent } from './reception-create.component';

describe('ReceptionCreateComponent', () => {
  let component: ReceptionCreateComponent;
  let fixture: ComponentFixture<ReceptionCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReceptionCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReceptionCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
