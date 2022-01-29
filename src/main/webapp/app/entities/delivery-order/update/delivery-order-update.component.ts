import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDeliveryOrder, DeliveryOrder } from '../delivery-order.model';
import { DeliveryOrderService } from '../service/delivery-order.service';
import { DeliveryOrderStatus } from 'app/entities/enumerations/delivery-order-status.model';

@Component({
  selector: 'jhi-delivery-order-update',
  templateUrl: './delivery-order-update.component.html',
})
export class DeliveryOrderUpdateComponent implements OnInit {
  isSaving = false;
  deliveryOrderStatusValues = Object.keys(DeliveryOrderStatus);

  editForm = this.fb.group({
    id: [],
    doNumber: [null, [Validators.required]],
    placedDate: [null, [Validators.required]],
    status: [null, [Validators.required]],
    code: [null, [Validators.required]],
  });

  constructor(protected deliveryOrderService: DeliveryOrderService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryOrder }) => {
      if (deliveryOrder.id === undefined) {
        const today = dayjs().startOf('day');
        deliveryOrder.placedDate = today;
      }

      this.updateForm(deliveryOrder);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliveryOrder = this.createFromForm();
    if (deliveryOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.deliveryOrderService.update(deliveryOrder));
    } else {
      this.subscribeToSaveResponse(this.deliveryOrderService.create(deliveryOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryOrder>>): void {
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

  protected updateForm(deliveryOrder: IDeliveryOrder): void {
    this.editForm.patchValue({
      id: deliveryOrder.id,
      doNumber: deliveryOrder.doNumber,
      placedDate: deliveryOrder.placedDate ? deliveryOrder.placedDate.format(DATE_TIME_FORMAT) : null,
      status: deliveryOrder.status,
      code: deliveryOrder.code,
    });
  }

  protected createFromForm(): IDeliveryOrder {
    return {
      ...new DeliveryOrder(),
      id: this.editForm.get(['id'])!.value,
      doNumber: this.editForm.get(['doNumber'])!.value,
      placedDate: this.editForm.get(['placedDate'])!.value ? dayjs(this.editForm.get(['placedDate'])!.value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status'])!.value,
      code: this.editForm.get(['code'])!.value,
    };
  }
}
