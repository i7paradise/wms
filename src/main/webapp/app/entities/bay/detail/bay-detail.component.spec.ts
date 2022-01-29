import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BayDetailComponent } from './bay-detail.component';

describe('Bay Management Detail Component', () => {
  let comp: BayDetailComponent;
  let fixture: ComponentFixture<BayDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BayDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bay: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BayDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BayDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bay on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bay).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
