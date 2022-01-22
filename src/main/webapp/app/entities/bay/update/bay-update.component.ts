import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBay, Bay } from '../bay.model';
import { BayService } from '../service/bay.service';
import { IWHRow } from 'app/entities/wh-row/wh-row.model';
import { WHRowService } from 'app/entities/wh-row/service/wh-row.service';

@Component({
  selector: 'jhi-bay-update',
  templateUrl: './bay-update.component.html',
})
export class BayUpdateComponent implements OnInit {
  isSaving = false;

  wHRowsSharedCollection: IWHRow[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    note: [],
    whrow: [],
  });

  constructor(
    protected bayService: BayService,
    protected wHRowService: WHRowService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bay }) => {
      this.updateForm(bay);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bay = this.createFromForm();
    if (bay.id !== undefined) {
      this.subscribeToSaveResponse(this.bayService.update(bay));
    } else {
      this.subscribeToSaveResponse(this.bayService.create(bay));
    }
  }

  trackWHRowById(index: number, item: IWHRow): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBay>>): void {
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

  protected updateForm(bay: IBay): void {
    this.editForm.patchValue({
      id: bay.id,
      name: bay.name,
      note: bay.note,
      whrow: bay.whrow,
    });

    this.wHRowsSharedCollection = this.wHRowService.addWHRowToCollectionIfMissing(this.wHRowsSharedCollection, bay.whrow);
  }

  protected loadRelationshipsOptions(): void {
    this.wHRowService
      .query()
      .pipe(map((res: HttpResponse<IWHRow[]>) => res.body ?? []))
      .pipe(map((wHRows: IWHRow[]) => this.wHRowService.addWHRowToCollectionIfMissing(wHRows, this.editForm.get('whrow')!.value)))
      .subscribe((wHRows: IWHRow[]) => (this.wHRowsSharedCollection = wHRows));
  }

  protected createFromForm(): IBay {
    return {
      ...new Bay(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      note: this.editForm.get(['note'])!.value,
      whrow: this.editForm.get(['whrow'])!.value,
    };
  }
}
