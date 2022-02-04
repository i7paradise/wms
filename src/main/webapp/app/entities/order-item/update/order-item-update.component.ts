import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrderItem, OrderItem } from '../order-item.model';
import { OrderItemService } from '../service/order-item.service';
import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { CompanyProductService } from 'app/entities/company-product/service/company-product.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

@Component({
  selector: 'jhi-order-item-update',
  templateUrl: './order-item-update.component.html',
})
export class OrderItemUpdateComponent implements OnInit {
  isSaving = false;
  orderItemStatusValues = Object.keys(OrderItemStatus);

  compganyProductsCollection: ICompanyProduct[] = [];
  ordersSharedCollection: IOrder[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required, Validators.min(0)]],
    status: [null, [Validators.required]],
    compganyProduct: [],
    order: [],
  });

  constructor(
    protected orderItemService: OrderItemService,
    protected companyProductService: CompanyProductService,
    protected orderService: OrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItem }) => {
      this.updateForm(orderItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderItem = this.createFromForm();
    if (orderItem.id !== undefined) {
      this.subscribeToSaveResponse(this.orderItemService.update(orderItem));
    } else {
      this.subscribeToSaveResponse(this.orderItemService.create(orderItem));
    }
  }

  trackCompanyProductById(index: number, item: ICompanyProduct): number {
    return item.id!;
  }

  trackOrderById(index: number, item: IOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderItem>>): void {
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

  protected updateForm(orderItem: IOrderItem): void {
    this.editForm.patchValue({
      id: orderItem.id,
      quantity: orderItem.quantity,
      status: orderItem.status,
      compganyProduct: orderItem.compganyProduct,
      order: orderItem.order,
    });

    this.compganyProductsCollection = this.companyProductService.addCompanyProductToCollectionIfMissing(
      this.compganyProductsCollection,
      orderItem.compganyProduct
    );
    this.ordersSharedCollection = this.orderService.addOrderToCollectionIfMissing(this.ordersSharedCollection, orderItem.order);
  }

  protected loadRelationshipsOptions(): void {
    this.companyProductService
      .query({ filter: 'orderitem-is-null' })
      .pipe(map((res: HttpResponse<ICompanyProduct[]>) => res.body ?? []))
      .pipe(
        map((companyProducts: ICompanyProduct[]) =>
          this.companyProductService.addCompanyProductToCollectionIfMissing(companyProducts, this.editForm.get('compganyProduct')!.value)
        )
      )
      .subscribe((companyProducts: ICompanyProduct[]) => (this.compganyProductsCollection = companyProducts));

    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing(orders, this.editForm.get('order')!.value)))
      .subscribe((orders: IOrder[]) => (this.ordersSharedCollection = orders));
  }

  protected createFromForm(): IOrderItem {
    return {
      ...new OrderItem(),
      id: this.editForm.get(['id'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      status: this.editForm.get(['status'])!.value,
      compganyProduct: this.editForm.get(['compganyProduct'])!.value,
      order: this.editForm.get(['order'])!.value,
    };
  }
}
