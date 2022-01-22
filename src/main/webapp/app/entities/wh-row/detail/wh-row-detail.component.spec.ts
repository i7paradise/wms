import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WHRowDetailComponent } from './wh-row-detail.component';

describe('WHRow Management Detail Component', () => {
  let comp: WHRowDetailComponent;
  let fixture: ComponentFixture<WHRowDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WHRowDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ wHRow: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WHRowDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WHRowDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load wHRow on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.wHRow).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
