import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UHFRFIDAntennaDetailComponent } from './uhfrfid-antenna-detail.component';

describe('UHFRFIDAntenna Management Detail Component', () => {
  let comp: UHFRFIDAntennaDetailComponent;
  let fixture: ComponentFixture<UHFRFIDAntennaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UHFRFIDAntennaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ uHFRFIDAntenna: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UHFRFIDAntennaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UHFRFIDAntennaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load uHFRFIDAntenna on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.uHFRFIDAntenna).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
