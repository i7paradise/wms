import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContainerDetailComponent } from './container-detail.component';

describe('Container Management Detail Component', () => {
  let comp: ContainerDetailComponent;
  let fixture: ComponentFixture<ContainerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContainerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ container: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContainerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContainerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load container on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.container).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
