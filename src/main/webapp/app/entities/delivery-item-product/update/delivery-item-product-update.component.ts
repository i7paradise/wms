import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDeliveryItemProduct, DeliveryItemProduct } from '../delivery-item-product.model';
import { DeliveryItemProductService } from '../service/delivery-item-product.service';
import { IDeliveryContainer } from 'app/entities/delivery-container/delivery-container.model';
import { DeliveryContainerService } from 'app/entities/delivery-container/service/delivery-container.service';

@Component({
  selector: 'jhi-delivery-item-product-update',
  templateUrl: './delivery-item-product-update.component.html',
})
export class DeliveryItemProductUpdateComponent implements OnInit {
  isSaving = false;

  deliveryContainersSharedCollection: IDeliveryContainer[] = [];

  editForm = this.fb.group({
    id: [],
    rfidTAG: [null, [Validators.required]],
    deliveryContainer: [],
  });

  constructor(
    protected deliveryItemProductService: DeliveryItemProductService,
    protected deliveryContainerService: DeliveryContainerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryItemProduct }) => {
      this.updateForm(deliveryItemProduct);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliveryItemProduct = this.createFromForm();
    if (deliveryItemProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.deliveryItemProductService.update(deliveryItemProduct));
    } else {
      this.subscribeToSaveResponse(this.deliveryItemProductService.create(deliveryItemProduct));
    }
  }

  trackDeliveryContainerById(index: number, item: IDeliveryContainer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryItemProduct>>): void {
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

  protected updateForm(deliveryItemProduct: IDeliveryItemProduct): void {
    this.editForm.patchValue({
      id: deliveryItemProduct.id,
      rfidTAG: deliveryItemProduct.rfidTAG,
      deliveryContainer: deliveryItemProduct.deliveryContainer,
    });

    this.deliveryContainersSharedCollection = this.deliveryContainerService.addDeliveryContainerToCollectionIfMissing(
      this.deliveryContainersSharedCollection,
      deliveryItemProduct.deliveryContainer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deliveryContainerService
      .query()
      .pipe(map((res: HttpResponse<IDeliveryContainer[]>) => res.body ?? []))
      .pipe(
        map((deliveryContainers: IDeliveryContainer[]) =>
          this.deliveryContainerService.addDeliveryContainerToCollectionIfMissing(
            deliveryContainers,
            this.editForm.get('deliveryContainer')!.value
          )
        )
      )
      .subscribe((deliveryContainers: IDeliveryContainer[]) => (this.deliveryContainersSharedCollection = deliveryContainers));
  }

  protected createFromForm(): IDeliveryItemProduct {
    return {
      ...new DeliveryItemProduct(),
      id: this.editForm.get(['id'])!.value,
      rfidTAG: this.editForm.get(['rfidTAG'])!.value,
      deliveryContainer: this.editForm.get(['deliveryContainer'])!.value,
    };
  }
}
