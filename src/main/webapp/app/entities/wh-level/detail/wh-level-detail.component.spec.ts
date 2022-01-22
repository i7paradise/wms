import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WHLevelDetailComponent } from './wh-level-detail.component';

describe('WHLevel Management Detail Component', () => {
  let comp: WHLevelDetailComponent;
  let fixture: ComponentFixture<WHLevelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WHLevelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ wHLevel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WHLevelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WHLevelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load wHLevel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.wHLevel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
