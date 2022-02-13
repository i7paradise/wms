import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContainerCategory, ContainerCategory } from '../container-category.model';
import { ContainerCategoryService } from '../service/container-category.service';

@Component({
  selector: 'jhi-container-category-update',
  templateUrl: './container-category-update.component.html',
})
export class ContainerCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
  });

  constructor(
    protected containerCategoryService: ContainerCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ containerCategory }) => {
      this.updateForm(containerCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const containerCategory = this.createFromForm();
    if (containerCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.containerCategoryService.update(containerCategory));
    } else {
      this.subscribeToSaveResponse(this.containerCategoryService.create(containerCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContainerCategory>>): void {
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

  protected updateForm(containerCategory: IContainerCategory): void {
    this.editForm.patchValue({
      id: containerCategory.id,
      name: containerCategory.name,
      description: containerCategory.description,
    });
  }

  protected createFromForm(): IContainerCategory {
    return {
      ...new ContainerCategory(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
