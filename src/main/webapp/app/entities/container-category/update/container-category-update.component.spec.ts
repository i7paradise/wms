import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContainerCategoryService } from '../service/container-category.service';
import { IContainerCategory, ContainerCategory } from '../container-category.model';

import { ContainerCategoryUpdateComponent } from './container-category-update.component';

describe('ContainerCategory Management Update Component', () => {
  let comp: ContainerCategoryUpdateComponent;
  let fixture: ComponentFixture<ContainerCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let containerCategoryService: ContainerCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContainerCategoryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ContainerCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContainerCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    containerCategoryService = TestBed.inject(ContainerCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const containerCategory: IContainerCategory = { id: 456 };

      activatedRoute.data = of({ containerCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(containerCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContainerCategory>>();
      const containerCategory = { id: 123 };
      jest.spyOn(containerCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ containerCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: containerCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(containerCategoryService.update).toHaveBeenCalledWith(containerCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContainerCategory>>();
      const containerCategory = new ContainerCategory();
      jest.spyOn(containerCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ containerCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: containerCategory }));
      saveSubject.complete();

      // THEN
      expect(containerCategoryService.create).toHaveBeenCalledWith(containerCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContainerCategory>>();
      const containerCategory = { id: 123 };
      jest.spyOn(containerCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ containerCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(containerCategoryService.update).toHaveBeenCalledWith(containerCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
