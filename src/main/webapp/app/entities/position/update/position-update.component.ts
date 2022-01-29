import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPosition, Position } from '../position.model';
import { PositionService } from '../service/position.service';
import { IWHLevel } from 'app/entities/wh-level/wh-level.model';
import { WHLevelService } from 'app/entities/wh-level/service/wh-level.service';

@Component({
  selector: 'jhi-position-update',
  templateUrl: './position-update.component.html',
})
export class PositionUpdateComponent implements OnInit {
  isSaving = false;

  wHLevelsSharedCollection: IWHLevel[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    note: [],
    whlevel: [],
  });

  constructor(
    protected positionService: PositionService,
    protected wHLevelService: WHLevelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ position }) => {
      this.updateForm(position);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const position = this.createFromForm();
    if (position.id !== undefined) {
      this.subscribeToSaveResponse(this.positionService.update(position));
    } else {
      this.subscribeToSaveResponse(this.positionService.create(position));
    }
  }

  trackWHLevelById(index: number, item: IWHLevel): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPosition>>): void {
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

  protected updateForm(position: IPosition): void {
    this.editForm.patchValue({
      id: position.id,
      name: position.name,
      note: position.note,
      whlevel: position.whlevel,
    });

    this.wHLevelsSharedCollection = this.wHLevelService.addWHLevelToCollectionIfMissing(this.wHLevelsSharedCollection, position.whlevel);
  }

  protected loadRelationshipsOptions(): void {
    this.wHLevelService
      .query()
      .pipe(map((res: HttpResponse<IWHLevel[]>) => res.body ?? []))
      .pipe(
        map((wHLevels: IWHLevel[]) => this.wHLevelService.addWHLevelToCollectionIfMissing(wHLevels, this.editForm.get('whlevel')!.value))
      )
      .subscribe((wHLevels: IWHLevel[]) => (this.wHLevelsSharedCollection = wHLevels));
  }

  protected createFromForm(): IPosition {
    return {
      ...new Position(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      note: this.editForm.get(['note'])!.value,
      whlevel: this.editForm.get(['whlevel'])!.value,
    };
  }
}
