import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoorAntennaSelectorComponent } from './door-antenna-selector.component';

describe('DoorAntennaSelectorComponent', () => {
  let component: DoorAntennaSelectorComponent;
  let fixture: ComponentFixture<DoorAntennaSelectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DoorAntennaSelectorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DoorAntennaSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
