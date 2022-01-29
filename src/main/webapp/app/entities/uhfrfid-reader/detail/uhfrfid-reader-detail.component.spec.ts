import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UHFRFIDReaderDetailComponent } from './uhfrfid-reader-detail.component';

describe('UHFRFIDReader Management Detail Component', () => {
  let comp: UHFRFIDReaderDetailComponent;
  let fixture: ComponentFixture<UHFRFIDReaderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UHFRFIDReaderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ uHFRFIDReader: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UHFRFIDReaderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UHFRFIDReaderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load uHFRFIDReader on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.uHFRFIDReader).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
