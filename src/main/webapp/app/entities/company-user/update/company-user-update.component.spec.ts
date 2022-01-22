import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompanyUserService } from '../service/company-user.service';
import { ICompanyUser, CompanyUser } from '../company-user.model';

import { CompanyUserUpdateComponent } from './company-user-update.component';

describe('CompanyUser Management Update Component', () => {
  let comp: CompanyUserUpdateComponent;
  let fixture: ComponentFixture<CompanyUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let companyUserService: CompanyUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompanyUserUpdateComponent],
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
      .overrideTemplate(CompanyUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompanyUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    companyUserService = TestBed.inject(CompanyUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const companyUser: ICompanyUser = { id: 456 };

      activatedRoute.data = of({ companyUser });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(companyUser));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompanyUser>>();
      const companyUser = { id: 123 };
      jest.spyOn(companyUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ companyUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: companyUser }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(companyUserService.update).toHaveBeenCalledWith(companyUser);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompanyUser>>();
      const companyUser = new CompanyUser();
      jest.spyOn(companyUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ companyUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: companyUser }));
      saveSubject.complete();

      // THEN
      expect(companyUserService.create).toHaveBeenCalledWith(companyUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompanyUser>>();
      const companyUser = { id: 123 };
      jest.spyOn(companyUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ companyUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(companyUserService.update).toHaveBeenCalledWith(companyUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
