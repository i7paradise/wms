import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOrderContainer, OrderContainer } from '../order-container.model';
import { OrderContainerService } from '../service/order-container.service';

@Component({
  selector: 'jhi-order-container-update',
  templateUrl: './order-container-update.component.html',
})
export class OrderContainerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    supplierRFIDTag: [],
  });

  constructor(
    protected orderContainerService: OrderContainerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderContainer }) => {
      this.updateForm(orderContainer);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderContainer = this.createFromForm();
    if (orderContainer.id !== undefined) {
      this.subscribeToSaveResponse(this.orderContainerService.update(orderContainer));
    } else {
      this.subscribeToSaveResponse(this.orderContainerService.create(orderContainer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderContainer>>): void {
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

  protected updateForm(orderContainer: IOrderContainer): void {
    this.editForm.patchValue({
      id: orderContainer.id,
      supplierRFIDTag: orderContainer.supplierRFIDTag,
    });
  }

  protected createFromForm(): IOrderContainer {
    return {
      ...new OrderContainer(),
      id: this.editForm.get(['id'])!.value,
      supplierRFIDTag: this.editForm.get(['supplierRFIDTag'])!.value,
    };
  }
}
