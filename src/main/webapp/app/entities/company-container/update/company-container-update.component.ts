import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompanyContainer, CompanyContainer } from '../company-container.model';
import { CompanyContainerService } from '../service/company-container.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IContainer } from 'app/entities/container/container.model';
import { ContainerService } from 'app/entities/container/service/container.service';

@Component({
  selector: 'jhi-company-container-update',
  templateUrl: './company-container-update.component.html',
})
export class CompanyContainerUpdateComponent implements OnInit {
  isSaving = false;

  companiesSharedCollection: ICompany[] = [];
  containersSharedCollection: IContainer[] = [];

  editForm = this.fb.group({
    id: [],
    rfidTag: [],
    color: [],
    company: [],
    container: [],
  });

  constructor(
    protected companyContainerService: CompanyContainerService,
    protected companyService: CompanyService,
    protected containerService: ContainerService,
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

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  trackContainerById(index: number, item: IContainer): number {
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
      company: companyContainer.company,
      container: companyContainer.container,
    });

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(
      this.companiesSharedCollection,
      companyContainer.company
    );
    this.containersSharedCollection = this.containerService.addContainerToCollectionIfMissing(
      this.containersSharedCollection,
      companyContainer.container
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.containerService
      .query()
      .pipe(map((res: HttpResponse<IContainer[]>) => res.body ?? []))
      .pipe(
        map((containers: IContainer[]) =>
          this.containerService.addContainerToCollectionIfMissing(containers, this.editForm.get('container')!.value)
        )
      )
      .subscribe((containers: IContainer[]) => (this.containersSharedCollection = containers));
  }

  protected createFromForm(): ICompanyContainer {
    return {
      ...new CompanyContainer(),
      id: this.editForm.get(['id'])!.value,
      rfidTag: this.editForm.get(['rfidTag'])!.value,
      color: this.editForm.get(['color'])!.value,
      company: this.editForm.get(['company'])!.value,
      container: this.editForm.get(['container'])!.value,
    };
  }
}
