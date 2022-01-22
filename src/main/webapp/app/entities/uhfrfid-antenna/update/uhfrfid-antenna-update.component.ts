import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUHFRFIDAntenna, UHFRFIDAntenna } from '../uhfrfid-antenna.model';
import { UHFRFIDAntennaService } from '../service/uhfrfid-antenna.service';
import { IUHFRFIDReader } from 'app/entities/uhfrfid-reader/uhfrfid-reader.model';
import { UHFRFIDReaderService } from 'app/entities/uhfrfid-reader/service/uhfrfid-reader.service';
import { UHFRFIDAntennaStatus } from 'app/entities/enumerations/uhfrfid-antenna-status.model';

@Component({
  selector: 'jhi-uhfrfid-antenna-update',
  templateUrl: './uhfrfid-antenna-update.component.html',
})
export class UHFRFIDAntennaUpdateComponent implements OnInit {
  isSaving = false;
  uHFRFIDAntennaStatusValues = Object.keys(UHFRFIDAntennaStatus);

  uHFRFIDReadersSharedCollection: IUHFRFIDReader[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    outputPower: [null, [Validators.required]],
    status: [null, [Validators.required]],
    uhfReader: [],
  });

  constructor(
    protected uHFRFIDAntennaService: UHFRFIDAntennaService,
    protected uHFRFIDReaderService: UHFRFIDReaderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uHFRFIDAntenna }) => {
      this.updateForm(uHFRFIDAntenna);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const uHFRFIDAntenna = this.createFromForm();
    if (uHFRFIDAntenna.id !== undefined) {
      this.subscribeToSaveResponse(this.uHFRFIDAntennaService.update(uHFRFIDAntenna));
    } else {
      this.subscribeToSaveResponse(this.uHFRFIDAntennaService.create(uHFRFIDAntenna));
    }
  }

  trackUHFRFIDReaderById(index: number, item: IUHFRFIDReader): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUHFRFIDAntenna>>): void {
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

  protected updateForm(uHFRFIDAntenna: IUHFRFIDAntenna): void {
    this.editForm.patchValue({
      id: uHFRFIDAntenna.id,
      name: uHFRFIDAntenna.name,
      outputPower: uHFRFIDAntenna.outputPower,
      status: uHFRFIDAntenna.status,
      uhfReader: uHFRFIDAntenna.uhfReader,
    });

    this.uHFRFIDReadersSharedCollection = this.uHFRFIDReaderService.addUHFRFIDReaderToCollectionIfMissing(
      this.uHFRFIDReadersSharedCollection,
      uHFRFIDAntenna.uhfReader
    );
  }

  protected loadRelationshipsOptions(): void {
    this.uHFRFIDReaderService
      .query()
      .pipe(map((res: HttpResponse<IUHFRFIDReader[]>) => res.body ?? []))
      .pipe(
        map((uHFRFIDReaders: IUHFRFIDReader[]) =>
          this.uHFRFIDReaderService.addUHFRFIDReaderToCollectionIfMissing(uHFRFIDReaders, this.editForm.get('uhfReader')!.value)
        )
      )
      .subscribe((uHFRFIDReaders: IUHFRFIDReader[]) => (this.uHFRFIDReadersSharedCollection = uHFRFIDReaders));
  }

  protected createFromForm(): IUHFRFIDAntenna {
    return {
      ...new UHFRFIDAntenna(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      outputPower: this.editForm.get(['outputPower'])!.value,
      status: this.editForm.get(['status'])!.value,
      uhfReader: this.editForm.get(['uhfReader'])!.value,
    };
  }
}
