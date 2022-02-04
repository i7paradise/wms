import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrderItemProduct, OrderItemProduct } from '../order-item-product.model';
import { OrderItemProductService } from '../service/order-item-product.service';
import { IContainerCategory } from 'app/entities/container-category/container-category.model';
import { ContainerCategoryService } from 'app/entities/container-category/service/container-category.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';

@Component({
  selector: 'jhi-order-item-product-update',
  templateUrl: './order-item-product-update.component.html',
})
export class OrderItemProductUpdateComponent implements OnInit {
  isSaving = false;

  containerCategoriesSharedCollection: IContainerCategory[] = [];
  orderItemsSharedCollection: IOrderItem[] = [];

  editForm = this.fb.group({
    id: [],
    rfidTAG: [null, [Validators.required]],
    containerCategory: [],
    orderItem: [],
  });

  constructor(
    protected orderItemProductService: OrderItemProductService,
    protected containerCategoryService: ContainerCategoryService,
    protected orderItemService: OrderItemService,
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

  trackContainerCategoryById(index: number, item: IContainerCategory): number {
    return item.id!;
  }

  trackOrderItemById(index: number, item: IOrderItem): number {
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
      containerCategory: orderItemProduct.containerCategory,
      orderItem: orderItemProduct.orderItem,
    });

    this.containerCategoriesSharedCollection = this.containerCategoryService.addContainerCategoryToCollectionIfMissing(
      this.containerCategoriesSharedCollection,
      orderItemProduct.containerCategory
    );
    this.orderItemsSharedCollection = this.orderItemService.addOrderItemToCollectionIfMissing(
      this.orderItemsSharedCollection,
      orderItemProduct.orderItem
    );
  }

  protected loadRelationshipsOptions(): void {
    this.containerCategoryService
      .query()
      .pipe(map((res: HttpResponse<IContainerCategory[]>) => res.body ?? []))
      .pipe(
        map((containerCategories: IContainerCategory[]) =>
          this.containerCategoryService.addContainerCategoryToCollectionIfMissing(
            containerCategories,
            this.editForm.get('containerCategory')!.value
          )
        )
      )
      .subscribe((containerCategories: IContainerCategory[]) => (this.containerCategoriesSharedCollection = containerCategories));

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing(orderItems, this.editForm.get('orderItem')!.value)
        )
      )
      .subscribe((orderItems: IOrderItem[]) => (this.orderItemsSharedCollection = orderItems));
  }

  protected createFromForm(): IOrderItemProduct {
    return {
      ...new OrderItemProduct(),
      id: this.editForm.get(['id'])!.value,
      rfidTAG: this.editForm.get(['rfidTAG'])!.value,
      containerCategory: this.editForm.get(['containerCategory'])!.value,
      orderItem: this.editForm.get(['orderItem'])!.value,
    };
  }
}
