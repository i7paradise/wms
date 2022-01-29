import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DoorAntennaDetailComponent } from './door-antenna-detail.component';

describe('DoorAntenna Management Detail Component', () => {
  let comp: DoorAntennaDetailComponent;
  let fixture: ComponentFixture<DoorAntennaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoorAntennaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ doorAntenna: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DoorAntennaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DoorAntennaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load doorAntenna on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.doorAntenna).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
