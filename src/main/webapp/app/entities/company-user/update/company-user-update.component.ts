import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompanyUser, CompanyUser } from '../company-user.model';
import { CompanyUserService } from '../service/company-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

@Component({
  selector: 'jhi-company-user-update',
  templateUrl: './company-user-update.component.html',
})
export class CompanyUserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  companiesSharedCollection: ICompany[] = [];

  editForm = this.fb.group({
    id: [],
    user: [],
    company: [],
  });

  constructor(
    protected companyUserService: CompanyUserService,
    protected userService: UserService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyUser }) => {
      this.updateForm(companyUser);

      this.loadRelationshipsOptions();
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

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
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
      user: companyUser.user,
      company: companyUser.company,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, companyUser.user);
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(
      this.companiesSharedCollection,
      companyUser.company
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }

  protected createFromForm(): ICompanyUser {
    return {
      ...new CompanyUser(),
      id: this.editForm.get(['id'])!.value,
      user: this.editForm.get(['user'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }
}
