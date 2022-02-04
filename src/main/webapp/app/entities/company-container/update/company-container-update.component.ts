import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompanyContainer, CompanyContainer } from '../company-container.model';
import { CompanyContainerService } from '../service/company-container.service';
import { IContainerCategory } from 'app/entities/container-category/container-category.model';
import { ContainerCategoryService } from 'app/entities/container-category/service/container-category.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

@Component({
  selector: 'jhi-company-container-update',
  templateUrl: './company-container-update.component.html',
})
export class CompanyContainerUpdateComponent implements OnInit {
  isSaving = false;

  containerCategoriesCollection: IContainerCategory[] = [];
  companiesSharedCollection: ICompany[] = [];

  editForm = this.fb.group({
    id: [],
    rfidTag: [],
    color: [],
    containerCategory: [],
    company: [],
  });

  constructor(
    protected companyContainerService: CompanyContainerService,
    protected containerCategoryService: ContainerCategoryService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyContainer }) => {
      this.updateForm(companyContainer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const companyContainer = this.createFromForm();
    if (companyContainer.id !== undefined) {
      this.subscribeToSaveResponse(this.companyContainerService.update(companyContainer));
    } else {
      this.subscribeToSaveResponse(this.companyContainerService.create(companyContainer));
    }
  }

  trackContainerCategoryById(index: number, item: IContainerCategory): number {
    return item.id!;
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompanyContainer>>): void {
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

  protected updateForm(companyContainer: ICompanyContainer): void {
    this.editForm.patchValue({
      id: companyContainer.id,
      rfidTag: companyContainer.rfidTag,
      color: companyContainer.color,
      containerCategory: companyContainer.containerCategory,
      company: companyContainer.company,
    });

    this.containerCategoriesCollection = this.containerCategoryService.addContainerCategoryToCollectionIfMissing(
      this.containerCategoriesCollection,
      companyContainer.containerCategory
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(
      this.companiesSharedCollection,
      companyContainer.company
    );
  }

  protected loadRelationshipsOptions(): void {
    this.containerCategoryService
      .query({ filter: 'companycontainer-is-null' })
      .pipe(map((res: HttpResponse<IContainerCategory[]>) => res.body ?? []))
      .pipe(
        map((containerCategories: IContainerCategory[]) =>
          this.containerCategoryService.addContainerCategoryToCollectionIfMissing(
            containerCategories,
            this.editForm.get('containerCategory')!.value
          )
        )
      )
      .subscribe((containerCategories: IContainerCategory[]) => (this.containerCategoriesCollection = containerCategories));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }

  protected createFromForm(): ICompanyContainer {
    return {
      ...new CompanyContainer(),
      id: this.editForm.get(['id'])!.value,
      rfidTag: this.editForm.get(['rfidTag'])!.value,
      color: this.editForm.get(['color'])!.value,
      containerCategory: this.editForm.get(['containerCategory'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }
}
