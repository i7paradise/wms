import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrderContainer, OrderContainer } from '../order-container.model';
import { OrderContainerService } from '../service/order-container.service';
import { ICompanyContainer } from 'app/entities/company-container/company-container.model';
import { CompanyContainerService } from 'app/entities/company-container/service/company-container.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';

@Component({
  selector: 'jhi-order-container-update',
  templateUrl: './order-container-update.component.html',
})
export class OrderContainerUpdateComponent implements OnInit {
  isSaving = false;

  companyContainersCollection: ICompanyContainer[] = [];
  orderItemsSharedCollection: IOrderItem[] = [];

  editForm = this.fb.group({
    id: [],
    supplierRFIDTag: [],
    companyContainer: [],
    orderItem: [],
  });

  constructor(
    protected orderContainerService: OrderContainerService,
    protected companyContainerService: CompanyContainerService,
    protected orderItemService: OrderItemService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderContainer }) => {
      this.updateForm(orderContainer);

      this.loadRelationshipsOptions();
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

  trackCompanyContainerById(index: number, item: ICompanyContainer): number {
    return item.id!;
  }

  trackOrderItemById(index: number, item: IOrderItem): number {
    return item.id!;
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
      companyContainer: orderContainer.companyContainer,
      orderItem: orderContainer.orderItem,
    });

    this.companyContainersCollection = this.companyContainerService.addCompanyContainerToCollectionIfMissing(
      this.companyContainersCollection,
      orderContainer.companyContainer
    );
    this.orderItemsSharedCollection = this.orderItemService.addOrderItemToCollectionIfMissing(
      this.orderItemsSharedCollection,
      orderContainer.orderItem
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companyContainerService
      .query({ filter: 'ordercontainer-is-null' })
      .pipe(map((res: HttpResponse<ICompanyContainer[]>) => res.body ?? []))
      .pipe(
        map((companyContainers: ICompanyContainer[]) =>
          this.companyContainerService.addCompanyContainerToCollectionIfMissing(
            companyContainers,
            this.editForm.get('companyContainer')!.value
          )
        )
      )
      .subscribe((companyContainers: ICompanyContainer[]) => (this.companyContainersCollection = companyContainers));

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

  protected createFromForm(): IOrderContainer {
    return {
      ...new OrderContainer(),
      id: this.editForm.get(['id'])!.value,
      supplierRFIDTag: this.editForm.get(['supplierRFIDTag'])!.value,
      companyContainer: this.editForm.get(['companyContainer'])!.value,
      orderItem: this.editForm.get(['orderItem'])!.value,
    };
  }
}
