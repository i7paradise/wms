import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContainerCategoryDetailComponent } from './container-category-detail.component';

describe('ContainerCategory Management Detail Component', () => {
  let comp: ContainerCategoryDetailComponent;
  let fixture: ComponentFixture<ContainerCategoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContainerCategoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ containerCategory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContainerCategoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContainerCategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load containerCategory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.containerCategory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
