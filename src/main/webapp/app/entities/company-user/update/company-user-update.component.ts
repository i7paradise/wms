import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICompanyUser, CompanyUser } from '../company-user.model';
import { CompanyUserService } from '../service/company-user.service';

@Component({
  selector: 'jhi-company-user-update',
  templateUrl: './company-user-update.component.html',
})
export class CompanyUserUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected companyUserService: CompanyUserService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyUser }) => {
      this.updateForm(companyUser);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const companyUser = this.createFromForm();
    if (companyUser.id !== undefined) {
      this.subscribeToSaveResponse(this.companyUserService.update(companyUser));
    } else {
      this.subscribeToSaveResponse(this.companyUserService.create(companyUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompanyUser>>): void {
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

  protected updateForm(companyUser: ICompanyUser): void {
    this.editForm.patchValue({
      id: companyUser.id,
    });
  }

  protected createFromForm(): ICompanyUser {
    return {
      ...new CompanyUser(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
