import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDoorAntenna, DoorAntenna } from '../door-antenna.model';
import { DoorAntennaService } from '../service/door-antenna.service';
import { IDoor } from 'app/entities/door/door.model';
import { DoorService } from 'app/entities/door/service/door.service';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { UHFRFIDAntennaService } from 'app/entities/uhfrfid-antenna/service/uhfrfid-antenna.service';
import { DoorAntennaType } from 'app/entities/enumerations/door-antenna-type.model';

@Component({
  selector: 'jhi-door-antenna-update',
  templateUrl: './door-antenna-update.component.html',
})
export class DoorAntennaUpdateComponent implements OnInit {
  isSaving = false;
  doorAntennaTypeValues = Object.keys(DoorAntennaType);

  doorsSharedCollection: IDoor[] = [];
  uHFRFIDAntennasSharedCollection: IUHFRFIDAntenna[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    door: [],
    rfidAntenna: [],
  });

  constructor(
    protected doorAntennaService: DoorAntennaService,
    protected doorService: DoorService,
    protected uHFRFIDAntennaService: UHFRFIDAntennaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ doorAntenna }) => {
      this.updateForm(doorAntenna);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const doorAntenna = this.createFromForm();
    if (doorAntenna.id !== undefined) {
      this.subscribeToSaveResponse(this.doorAntennaService.update(doorAntenna));
    } else {
      this.subscribeToSaveResponse(this.doorAntennaService.create(doorAntenna));
    }
  }

  trackDoorById(index: number, item: IDoor): number {
    return item.id!;
  }

  trackUHFRFIDAntennaById(index: number, item: IUHFRFIDAntenna): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDoorAntenna>>): void {
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

  protected updateForm(doorAntenna: IDoorAntenna): void {
    this.editForm.patchValue({
      id: doorAntenna.id,
      type: doorAntenna.type,
      door: doorAntenna.door,
      rfidAntenna: doorAntenna.rfidAntenna,
    });

    this.doorsSharedCollection = this.doorService.addDoorToCollectionIfMissing(this.doorsSharedCollection, doorAntenna.door);
    this.uHFRFIDAntennasSharedCollection = this.uHFRFIDAntennaService.addUHFRFIDAntennaToCollectionIfMissing(
      this.uHFRFIDAntennasSharedCollection,
      doorAntenna.rfidAntenna
    );
  }

  protected loadRelationshipsOptions(): void {
    this.doorService
      .query()
      .pipe(map((res: HttpResponse<IDoor[]>) => res.body ?? []))
      .pipe(map((doors: IDoor[]) => this.doorService.addDoorToCollectionIfMissing(doors, this.editForm.get('door')!.value)))
      .subscribe((doors: IDoor[]) => (this.doorsSharedCollection = doors));

    this.uHFRFIDAntennaService
      .query()
      .pipe(map((res: HttpResponse<IUHFRFIDAntenna[]>) => res.body ?? []))
      .pipe(
        map((uHFRFIDAntennas: IUHFRFIDAntenna[]) =>
          this.uHFRFIDAntennaService.addUHFRFIDAntennaToCollectionIfMissing(uHFRFIDAntennas, this.editForm.get('rfidAntenna')!.value)
        )
      )
      .subscribe((uHFRFIDAntennas: IUHFRFIDAntenna[]) => (this.uHFRFIDAntennasSharedCollection = uHFRFIDAntennas));
  }

  protected createFromForm(): IDoorAntenna {
    return {
      ...new DoorAntenna(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      door: this.editForm.get(['door'])!.value,
      rfidAntenna: this.editForm.get(['rfidAntenna'])!.value,
    };
  }
}
