import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DoorDetailComponent } from './door-detail.component';

describe('Door Management Detail Component', () => {
  let comp: DoorDetailComponent;
  let fixture: ComponentFixture<DoorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DoorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ door: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DoorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DoorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load door on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.door).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
