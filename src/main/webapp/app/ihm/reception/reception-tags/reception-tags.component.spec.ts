import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceptionTagsComponent } from './reception-tags.component';

describe('ReceptionTagsComponent', () => {
  let component: ReceptionTagsComponent;
  let fixture: ComponentFixture<ReceptionTagsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReceptionTagsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReceptionTagsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
