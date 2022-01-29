import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContainerService } from '../service/container.service';
import { IContainer, Container } from '../container.model';

import { ContainerUpdateComponent } from './container-update.component';

describe('Container Management Update Component', () => {
  let comp: ContainerUpdateComponent;
  let fixture: ComponentFixture<ContainerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let containerService: ContainerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContainerUpdateComponent],
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
      .overrideTemplate(ContainerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContainerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    containerService = TestBed.inject(ContainerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const container: IContainer = { id: 456 };

      activatedRoute.data = of({ container });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(container));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Container>>();
      const container = { id: 123 };
      jest.spyOn(containerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ container });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: container }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(containerService.update).toHaveBeenCalledWith(container);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Container>>();
      const container = new Container();
      jest.spyOn(containerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ container });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: container }));
      saveSubject.complete();

      // THEN
      expect(containerService.create).toHaveBeenCalledWith(container);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Container>>();
      const container = { id: 123 };
      jest.spyOn(containerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ container });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(containerService.update).toHaveBeenCalledWith(container);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
