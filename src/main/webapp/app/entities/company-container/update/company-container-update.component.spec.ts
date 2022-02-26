import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompanyContainerService } from '../service/company-container.service';
import { ICompanyContainer, CompanyContainer } from '../company-container.model';
import { IContainerCategory } from 'app/entities/container-category/container-category.model';
import { ContainerCategoryService } from 'app/entities/container-category/service/container-category.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

import { CompanyContainerUpdateComponent } from './company-container-update.component';

describe('CompanyContainer Management Update Component', () => {
  let comp: CompanyContainerUpdateComponent;
  let fixture: ComponentFixture<CompanyContainerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let companyContainerService: CompanyContainerService;
  let containerCategoryService: ContainerCategoryService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompanyContainerUpdateComponent],
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
      .overrideTemplate(CompanyContainerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompanyContainerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    companyContainerService = TestBed.inject(CompanyContainerService);
    containerCategoryService = TestBed.inject(ContainerCategoryService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call containerCategory query and add missing value', () => {
      const companyContainer: ICompanyContainer = { id: 456 };
      const containerCategory: IContainerCategory = { id: 17424 };
      companyContainer.containerCategory = containerCategory;

      const containerCategoryCollection: IContainerCategory[] = [{ id: 30762 }];
      jest.spyOn(containerCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: containerCategoryCollection })));
      const expectedCollection: IContainerCategory[] = [containerCategory, ...containerCategoryCollection];
      jest.spyOn(containerCategoryService, 'addContainerCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ companyContainer });
      comp.ngOnInit();

      expect(containerCategoryService.query).toHaveBeenCalled();
      expect(containerCategoryService.addContainerCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        containerCategoryCollection,
        containerCategory
      );
      expect(comp.containerCategoriesCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const companyContainer: ICompanyContainer = { id: 456 };
      const company: ICompany = { id: 55896 };
      companyContainer.company = company;

      const companyCollection: ICompany[] = [{ id: 75374 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ companyContainer });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, ...additionalCompanies);
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const companyContainer: ICompanyContainer = { id: 456 };
      const containerCategory: IContainerCategory = { id: 5565 };
      companyContainer.containerCategory = containerCategory;
      const company: ICompany = { id: 60865 };
      companyContainer.company = company;

      activatedRoute.data = of({ companyContainer });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(companyContainer));
      expect(comp.containerCategoriesCollection).toContain(containerCategory);
      expect(comp.companiesSharedCollection).toContain(company);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompanyContainer>>();
      const companyContainer = { id: 123 };
      jest.spyOn(companyContainerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ companyContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: companyContainer }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(companyContainerService.update).toHaveBeenCalledWith(companyContainer);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompanyContainer>>();
      const companyContainer = new CompanyContainer();
      jest.spyOn(companyContainerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ companyContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: companyContainer }));
      saveSubject.complete();

      // THEN
      expect(companyContainerService.create).toHaveBeenCalledWith(companyContainer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompanyContainer>>();
      const companyContainer = { id: 123 };
      jest.spyOn(companyContainerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ companyContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(companyContainerService.update).toHaveBeenCalledWith(companyContainer);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackContainerCategoryById', () => {
      it('Should return tracked ContainerCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContainerCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCompanyById', () => {
      it('Should return tracked Company primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompanyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
