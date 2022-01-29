import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWHRow, WHRow } from '../wh-row.model';
import { WHRowService } from '../service/wh-row.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';

@Component({
  selector: 'jhi-wh-row-update',
  templateUrl: './wh-row-update.component.html',
})
export class WHRowUpdateComponent implements OnInit {
  isSaving = false;

  locationsSharedCollection: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    note: [],
    location: [],
  });

  constructor(
    protected wHRowService: WHRowService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wHRow }) => {
      this.updateForm(wHRow);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wHRow = this.createFromForm();
    if (wHRow.id !== undefined) {
      this.subscribeToSaveResponse(this.wHRowService.update(wHRow));
    } else {
      this.subscribeToSaveResponse(this.wHRowService.create(wHRow));
    }
  }

  trackLocationById(index: number, item: ILocation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWHRow>>): void {
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

  protected updateForm(wHRow: IWHRow): void {
    this.editForm.patchValue({
      id: wHRow.id,
      name: wHRow.name,
      note: wHRow.note,
      location: wHRow.location,
    });

    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing(this.locationsSharedCollection, wHRow.location);
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing(locations, this.editForm.get('location')!.value)
        )
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));
  }

  protected createFromForm(): IWHRow {
    return {
      ...new WHRow(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      note: this.editForm.get(['note'])!.value,
      location: this.editForm.get(['location'])!.value,
    };
  }
}
