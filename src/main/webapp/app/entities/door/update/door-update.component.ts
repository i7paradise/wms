import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDoor, Door } from '../door.model';
import { DoorService } from '../service/door.service';
import { IArea } from 'app/entities/area/area.model';
import { AreaService } from 'app/entities/area/service/area.service';

@Component({
  selector: 'jhi-door-update',
  templateUrl: './door-update.component.html',
})
export class DoorUpdateComponent implements OnInit {
  isSaving = false;

  areasSharedCollection: IArea[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    area: [],
  });

  constructor(
    protected doorService: DoorService,
    protected areaService: AreaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ door }) => {
      this.updateForm(door);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const door = this.createFromForm();
    if (door.id !== undefined) {
      this.subscribeToSaveResponse(this.doorService.update(door));
    } else {
      this.subscribeToSaveResponse(this.doorService.create(door));
    }
  }

  trackAreaById(index: number, item: IArea): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDoor>>): void {
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

  protected updateForm(door: IDoor): void {
    this.editForm.patchValue({
      id: door.id,
      name: door.name,
      area: door.area,
    });

    this.areasSharedCollection = this.areaService.addAreaToCollectionIfMissing(this.areasSharedCollection, door.area);
  }

  protected loadRelationshipsOptions(): void {
    this.areaService
      .query()
      .pipe(map((res: HttpResponse<IArea[]>) => res.body ?? []))
      .pipe(map((areas: IArea[]) => this.areaService.addAreaToCollectionIfMissing(areas, this.editForm.get('area')!.value)))
      .subscribe((areas: IArea[]) => (this.areasSharedCollection = areas));
  }

  protected createFromForm(): IDoor {
    return {
      ...new Door(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      area: this.editForm.get(['area'])!.value,
    };
  }
}
