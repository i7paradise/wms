import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContainer, Container } from '../container.model';
import { ContainerService } from '../service/container.service';

@Component({
  selector: 'jhi-container-update',
  templateUrl: './container-update.component.html',
})
export class ContainerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
  });

  constructor(protected containerService: ContainerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ container }) => {
      this.updateForm(container);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const container = this.createFromForm();
    if (container.id !== undefined) {
      this.subscribeToSaveResponse(this.containerService.update(container));
    } else {
      this.subscribeToSaveResponse(this.containerService.create(container));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContainer>>): void {
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

  protected updateForm(container: IContainer): void {
    this.editForm.patchValue({
      id: container.id,
      name: container.name,
      description: container.description,
    });
  }

  protected createFromForm(): IContainer {
    return {
      ...new Container(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
