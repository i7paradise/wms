import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReceptionDetailComponent } from './reception-detail.component';

describe('ReceptionDetailComponent', () => {
  let comp: ReceptionDetailComponent;
  let fixture: ComponentFixture<ReceptionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReceptionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deliveryOrder: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReceptionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReceptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deliveryOrder on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deliveryOrder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
