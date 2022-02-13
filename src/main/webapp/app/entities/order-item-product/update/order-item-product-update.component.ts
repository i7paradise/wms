import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrderItemProduct, OrderItemProduct } from '../order-item-product.model';
import { OrderItemProductService } from '../service/order-item-product.service';
import { IOrderContainer } from 'app/entities/order-container/order-container.model';
import { OrderContainerService } from 'app/entities/order-container/service/order-container.service';

@Component({
  selector: 'jhi-order-item-product-update',
  templateUrl: './order-item-product-update.component.html',
})
export class OrderItemProductUpdateComponent implements OnInit {
  isSaving = false;

  orderContainersSharedCollection: IOrderContainer[] = [];

  editForm = this.fb.group({
    id: [],
    rfidTAG: [null, [Validators.required]],
    orderItem: [],
  });

  constructor(
    protected orderItemProductService: OrderItemProductService,
    protected orderContainerService: OrderContainerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItemProduct }) => {
      this.updateForm(orderItemProduct);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderItemProduct = this.createFromForm();
    if (orderItemProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.orderItemProductService.update(orderItemProduct));
    } else {
      this.subscribeToSaveResponse(this.orderItemProductService.create(orderItemProduct));
    }
  }

  trackOrderContainerById(index: number, item: IOrderContainer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderItemProduct>>): void {
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

  protected updateForm(orderItemProduct: IOrderItemProduct): void {
    this.editForm.patchValue({
      id: orderItemProduct.id,
      rfidTAG: orderItemProduct.rfidTAG,
      orderItem: orderItemProduct.orderItem,
    });

    this.orderContainersSharedCollection = this.orderContainerService.addOrderContainerToCollectionIfMissing(
      this.orderContainersSharedCollection,
      orderItemProduct.orderItem
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderContainerService
      .query()
      .pipe(map((res: HttpResponse<IOrderContainer[]>) => res.body ?? []))
      .pipe(
        map((orderContainers: IOrderContainer[]) =>
          this.orderContainerService.addOrderContainerToCollectionIfMissing(orderContainers, this.editForm.get('orderItem')!.value)
        )
      )
      .subscribe((orderContainers: IOrderContainer[]) => (this.orderContainersSharedCollection = orderContainers));
  }

  protected createFromForm(): IOrderItemProduct {
    return {
      ...new OrderItemProduct(),
      id: this.editForm.get(['id'])!.value,
      rfidTAG: this.editForm.get(['rfidTAG'])!.value,
      orderItem: this.editForm.get(['orderItem'])!.value,
    };
  }
}
