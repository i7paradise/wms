import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompanyProductService } from '../service/company-product.service';
import { ICompanyProduct, CompanyProduct } from '../company-product.model';
import { IContainer } from 'app/entities/container/container.model';
import { ContainerService } from 'app/entities/container/service/container.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { CompanyProductUpdateComponent } from './company-product-update.component';

describe('CompanyProduct Management Update Component', () => {
  let comp: CompanyProductUpdateComponent;
  let fixture: ComponentFixture<CompanyProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let companyProductService: CompanyProductService;
  let containerService: ContainerService;
  let companyService: CompanyService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompanyProductUpdateComponent],
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
      .overrideTemplate(CompanyProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompanyProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    companyProductService = TestBed.inject(CompanyProductService);
    containerService = TestBed.inject(ContainerService);
    companyService = TestBed.inject(CompanyService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call container query and add missing value', () => {
      const companyProduct: ICompanyProduct = { id: 456 };
      const container: IContainer = { id: 30441 };
      companyProduct.container = container;

      const containerCollection: IContainer[] = [{ id: 94604 }];
      jest.spyOn(containerService, 'query').mockReturnValue(of(new HttpResponse({ body: containerCollection })));
      const expectedCollection: IContainer[] = [container, ...containerCollection];
      jest.spyOn(containerService, 'addContainerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ companyProduct });
      comp.ngOnInit();

      expect(containerService.query).toHaveBeenCalled();
      expect(containerService.addContainerToCollectionIfMissing).toHaveBeenCalledWith(containerCollection, container);
      expect(comp.containersCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const companyProduct: ICompanyProduct = { id: 456 };
      const company: ICompany = { id: 40839 };
      companyProduct.company = company;

      const companyCollection: ICompany[] = [{ id: 88115 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ companyProduct });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, ...additionalCompanies);
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const companyProduct: ICompanyProduct = { id: 456 };
      const product: IProduct = { id: 42692 };
      companyProduct.product = product;

      const productCollection: IProduct[] = [{ id: 41793 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [product];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ companyProduct });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const companyProduct: ICompanyProduct = { id: 456 };
      const container: IContainer = { id: 25123 };
      companyProduct.container = container;
      const company: ICompany = { id: 74265 };
      companyProduct.company = company;
      const product: IProduct = { id: 23749 };
      companyProduct.product = product;

      activatedRoute.data = of({ companyProduct });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(companyProduct));
      expect(comp.containersCollection).toContain(container);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.productsSharedCollection).toContain(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompanyProduct>>();
      const companyProduct = { id: 123 };
      jest.spyOn(companyProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ companyProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: companyProduct }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(companyProductService.update).toHaveBeenCalledWith(companyProduct);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompanyProduct>>();
      const companyProduct = new CompanyProduct();
      jest.spyOn(companyProductService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ companyProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: companyProduct }));
      saveSubject.complete();

      // THEN
      expect(companyProductService.create).toHaveBeenCalledWith(companyProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CompanyProduct>>();
      const companyProduct = { id: 123 };
      jest.spyOn(companyProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ companyProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(companyProductService.update).toHaveBeenCalledWith(companyProduct);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackContainerById', () => {
      it('Should return tracked Container primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContainerById(0, entity);
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

    describe('trackProductById', () => {
      it('Should return tracked Product primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
