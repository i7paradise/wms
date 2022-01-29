import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUHFRFIDReader, UHFRFIDReader } from '../uhfrfid-reader.model';
import { UHFRFIDReaderService } from '../service/uhfrfid-reader.service';
import { UHFRFIDReaderStatus } from 'app/entities/enumerations/uhfrfid-reader-status.model';

@Component({
  selector: 'jhi-uhfrfid-reader-update',
  templateUrl: './uhfrfid-reader-update.component.html',
})
export class UHFRFIDReaderUpdateComponent implements OnInit {
  isSaving = false;
  uHFRFIDReaderStatusValues = Object.keys(UHFRFIDReaderStatus);

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    ip: [null, [Validators.required]],
    port: [null, [Validators.required]],
    status: [null, [Validators.required]],
  });

  constructor(protected uHFRFIDReaderService: UHFRFIDReaderService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uHFRFIDReader }) => {
      this.updateForm(uHFRFIDReader);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const uHFRFIDReader = this.createFromForm();
    if (uHFRFIDReader.id !== undefined) {
      this.subscribeToSaveResponse(this.uHFRFIDReaderService.update(uHFRFIDReader));
    } else {
      this.subscribeToSaveResponse(this.uHFRFIDReaderService.create(uHFRFIDReader));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUHFRFIDReader>>): void {
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

  protected updateForm(uHFRFIDReader: IUHFRFIDReader): void {
    this.editForm.patchValue({
      id: uHFRFIDReader.id,
      name: uHFRFIDReader.name,
      ip: uHFRFIDReader.ip,
      port: uHFRFIDReader.port,
      status: uHFRFIDReader.status,
    });
  }

  protected createFromForm(): IUHFRFIDReader {
    return {
      ...new UHFRFIDReader(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      ip: this.editForm.get(['ip'])!.value,
      port: this.editForm.get(['port'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
