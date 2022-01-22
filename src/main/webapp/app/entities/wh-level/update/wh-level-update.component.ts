import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWHLevel, WHLevel } from '../wh-level.model';
import { WHLevelService } from '../service/wh-level.service';
import { IBay } from 'app/entities/bay/bay.model';
import { BayService } from 'app/entities/bay/service/bay.service';

@Component({
  selector: 'jhi-wh-level-update',
  templateUrl: './wh-level-update.component.html',
})
export class WHLevelUpdateComponent implements OnInit {
  isSaving = false;

  baysSharedCollection: IBay[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    note: [],
    bay: [],
  });

  constructor(
    protected wHLevelService: WHLevelService,
    protected bayService: BayService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wHLevel }) => {
      this.updateForm(wHLevel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wHLevel = this.createFromForm();
    if (wHLevel.id !== undefined) {
      this.subscribeToSaveResponse(this.wHLevelService.update(wHLevel));
    } else {
      this.subscribeToSaveResponse(this.wHLevelService.create(wHLevel));
    }
  }

  trackBayById(index: number, item: IBay): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWHLevel>>): void {
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

  protected updateForm(wHLevel: IWHLevel): void {
    this.editForm.patchValue({
      id: wHLevel.id,
      name: wHLevel.name,
      note: wHLevel.note,
      bay: wHLevel.bay,
    });

    this.baysSharedCollection = this.bayService.addBayToCollectionIfMissing(this.baysSharedCollection, wHLevel.bay);
  }

  protected loadRelationshipsOptions(): void {
    this.bayService
      .query()
      .pipe(map((res: HttpResponse<IBay[]>) => res.body ?? []))
      .pipe(map((bays: IBay[]) => this.bayService.addBayToCollectionIfMissing(bays, this.editForm.get('bay')!.value)))
      .subscribe((bays: IBay[]) => (this.baysSharedCollection = bays));
  }

  protected createFromForm(): IWHLevel {
    return {
      ...new WHLevel(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      note: this.editForm.get(['note'])!.value,
      bay: this.editForm.get(['bay'])!.value,
    };
  }
}
