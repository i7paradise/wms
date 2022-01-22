import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompanyProduct, CompanyProduct } from '../company-product.model';
import { CompanyProductService } from '../service/company-product.service';
import { IContainer } from 'app/entities/container/container.model';
import { ContainerService } from 'app/entities/container/service/container.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-company-product-update',
  templateUrl: './company-product-update.component.html',
})
export class CompanyProductUpdateComponent implements OnInit {
  isSaving = false;

  containersCollection: IContainer[] = [];
  companiesSharedCollection: ICompany[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required, Validators.min(0)]],
    sku: [],
    stockingRatio: [null, [Validators.required, Validators.min(0)]],
    container: [],
    company: [],
    product: [],
  });

  constructor(
    protected companyProductService: CompanyProductService,
    protected containerService: ContainerService,
    protected companyService: CompanyService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyProduct }) => {
      this.updateForm(companyProduct);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const companyProduct = this.createFromForm();
    if (companyProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.companyProductService.update(companyProduct));
    } else {
      this.subscribeToSaveResponse(this.companyProductService.create(companyProduct));
    }
  }

  trackContainerById(index: number, item: IContainer): number {
    return item.id!;
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompanyProduct>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(companyProduct: ICompanyProduct): void {
    this.editForm.patchValue({
      id: companyProduct.id,
      quantity: companyProduct.quantity,
      sku: companyProduct.sku,
      stockingRatio: companyProduct.stockingRatio,
      container: companyProduct.container,
      company: companyProduct.company,
      product: companyProduct.product,
    });

    this.containersCollection = this.containerService.addContainerToCollectionIfMissing(
      this.containersCollection,
      companyProduct.container
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(
      this.companiesSharedCollection,
      companyProduct.company
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      companyProduct.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.containerService
      .query({ filter: 'companyproduct-is-null' })
      .pipe(map((res: HttpResponse<IContainer[]>) => res.body ?? []))
      .pipe(
        map((containers: IContainer[]) =>
          this.containerService.addContainerToCollectionIfMissing(containers, this.editForm.get('container')!.value)
        )
      )
      .subscribe((containers: IContainer[]) => (this.containersCollection = containers));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): ICompanyProduct {
    return {
      ...new CompanyProduct(),
      id: this.editForm.get(['id'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      sku: this.editForm.get(['sku'])!.value,
      stockingRatio: this.editForm.get(['stockingRatio'])!.value,
      container: this.editForm.get(['container'])!.value,
      company: this.editForm.get(['company'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }
}
