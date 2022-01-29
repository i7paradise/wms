import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDeliveryOrderItem, DeliveryOrderItem } from '../delivery-order-item.model';
import { DeliveryOrderItemService } from '../service/delivery-order-item.service';
import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { CompanyProductService } from 'app/entities/company-product/service/company-product.service';
import { IDeliveryOrder } from 'app/entities/delivery-order/delivery-order.model';
import { DeliveryOrderService } from 'app/entities/delivery-order/service/delivery-order.service';
import { DeliveryOrderItemStatus } from 'app/entities/enumerations/delivery-order-item-status.model';

@Component({
  selector: 'jhi-delivery-order-item-update',
  templateUrl: './delivery-order-item-update.component.html',
})
export class DeliveryOrderItemUpdateComponent implements OnInit {
  isSaving = false;
  deliveryOrderItemStatusValues = Object.keys(DeliveryOrderItemStatus);

  compganyProductsCollection: ICompanyProduct[] = [];
  deliveryOrdersSharedCollection: IDeliveryOrder[] = [];

  editForm = this.fb.group({
    id: [],
    unitQuantity: [null, [Validators.required, Validators.min(0)]],
    containerQuantity: [null, [Validators.required, Validators.min(0)]],
    status: [null, [Validators.required]],
    compganyProduct: [],
    deliveryOrder: [],
  });

  constructor(
    protected deliveryOrderItemService: DeliveryOrderItemService,
    protected companyProductService: CompanyProductService,
    protected deliveryOrderService: DeliveryOrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryOrderItem }) => {
      this.updateForm(deliveryOrderItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliveryOrderItem = this.createFromForm();
    if (deliveryOrderItem.id !== undefined) {
      this.subscribeToSaveResponse(this.deliveryOrderItemService.update(deliveryOrderItem));
    } else {
      this.subscribeToSaveResponse(this.deliveryOrderItemService.create(deliveryOrderItem));
    }
  }

  trackCompanyProductById(index: number, item: ICompanyProduct): number {
    return item.id!;
  }

  trackDeliveryOrderById(index: number, item: IDeliveryOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryOrderItem>>): void {
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

  protected updateForm(deliveryOrderItem: IDeliveryOrderItem): void {
    this.editForm.patchValue({
      id: deliveryOrderItem.id,
      unitQuantity: deliveryOrderItem.unitQuantity,
      containerQuantity: deliveryOrderItem.containerQuantity,
      status: deliveryOrderItem.status,
      compganyProduct: deliveryOrderItem.compganyProduct,
      deliveryOrder: deliveryOrderItem.deliveryOrder,
    });

    this.compganyProductsCollection = this.companyProductService.addCompanyProductToCollectionIfMissing(
      this.compganyProductsCollection,
      deliveryOrderItem.compganyProduct
    );
    this.deliveryOrdersSharedCollection = this.deliveryOrderService.addDeliveryOrderToCollectionIfMissing(
      this.deliveryOrdersSharedCollection,
      deliveryOrderItem.deliveryOrder
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companyProductService
      .query({ filter: 'deliveryorderitem-is-null' })
      .pipe(map((res: HttpResponse<ICompanyProduct[]>) => res.body ?? []))
      .pipe(
        map((companyProducts: ICompanyProduct[]) =>
          this.companyProductService.addCompanyProductToCollectionIfMissing(companyProducts, this.editForm.get('compganyProduct')!.value)
        )
      )
      .subscribe((companyProducts: ICompanyProduct[]) => (this.compganyProductsCollection = companyProducts));

    this.deliveryOrderService
      .query()
      .pipe(map((res: HttpResponse<IDeliveryOrder[]>) => res.body ?? []))
      .pipe(
        map((deliveryOrders: IDeliveryOrder[]) =>
          this.deliveryOrderService.addDeliveryOrderToCollectionIfMissing(deliveryOrders, this.editForm.get('deliveryOrder')!.value)
        )
      )
      .subscribe((deliveryOrders: IDeliveryOrder[]) => (this.deliveryOrdersSharedCollection = deliveryOrders));
  }

  protected createFromForm(): IDeliveryOrderItem {
    return {
      ...new DeliveryOrderItem(),
      id: this.editForm.get(['id'])!.value,
      unitQuantity: this.editForm.get(['unitQuantity'])!.value,
      containerQuantity: this.editForm.get(['containerQuantity'])!.value,
      status: this.editForm.get(['status'])!.value,
      compganyProduct: this.editForm.get(['compganyProduct'])!.value,
      deliveryOrder: this.editForm.get(['deliveryOrder'])!.value,
    };
  }
}
