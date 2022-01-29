import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDeliveryContainer, DeliveryContainer } from '../delivery-container.model';
import { DeliveryContainerService } from '../service/delivery-container.service';
import { IDeliveryOrderItem } from 'app/entities/delivery-order-item/delivery-order-item.model';
import { DeliveryOrderItemService } from 'app/entities/delivery-order-item/service/delivery-order-item.service';
import { ICompanyContainer } from 'app/entities/company-container/company-container.model';
import { CompanyContainerService } from 'app/entities/company-container/service/company-container.service';

@Component({
  selector: 'jhi-delivery-container-update',
  templateUrl: './delivery-container-update.component.html',
})
export class DeliveryContainerUpdateComponent implements OnInit {
  isSaving = false;

  deliveryOrderItemsSharedCollection: IDeliveryOrderItem[] = [];
  companyContainersSharedCollection: ICompanyContainer[] = [];

  editForm = this.fb.group({
    id: [],
    supplierRFIDTag: [],
    deliveryOrderItem: [],
    companyContainer: [],
  });

  constructor(
    protected deliveryContainerService: DeliveryContainerService,
    protected deliveryOrderItemService: DeliveryOrderItemService,
    protected companyContainerService: CompanyContainerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryContainer }) => {
      this.updateForm(deliveryContainer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliveryContainer = this.createFromForm();
    if (deliveryContainer.id !== undefined) {
      this.subscribeToSaveResponse(this.deliveryContainerService.update(deliveryContainer));
    } else {
      this.subscribeToSaveResponse(this.deliveryContainerService.create(deliveryContainer));
    }
  }

  trackDeliveryOrderItemById(index: number, item: IDeliveryOrderItem): number {
    return item.id!;
  }

  trackCompanyContainerById(index: number, item: ICompanyContainer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryContainer>>): void {
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

  protected updateForm(deliveryContainer: IDeliveryContainer): void {
    this.editForm.patchValue({
      id: deliveryContainer.id,
      supplierRFIDTag: deliveryContainer.supplierRFIDTag,
      deliveryOrderItem: deliveryContainer.deliveryOrderItem,
      companyContainer: deliveryContainer.companyContainer,
    });

    this.deliveryOrderItemsSharedCollection = this.deliveryOrderItemService.addDeliveryOrderItemToCollectionIfMissing(
      this.deliveryOrderItemsSharedCollection,
      deliveryContainer.deliveryOrderItem
    );
    this.companyContainersSharedCollection = this.companyContainerService.addCompanyContainerToCollectionIfMissing(
      this.companyContainersSharedCollection,
      deliveryContainer.companyContainer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deliveryOrderItemService
      .query()
      .pipe(map((res: HttpResponse<IDeliveryOrderItem[]>) => res.body ?? []))
      .pipe(
        map((deliveryOrderItems: IDeliveryOrderItem[]) =>
          this.deliveryOrderItemService.addDeliveryOrderItemToCollectionIfMissing(
            deliveryOrderItems,
            this.editForm.get('deliveryOrderItem')!.value
          )
        )
      )
      .subscribe((deliveryOrderItems: IDeliveryOrderItem[]) => (this.deliveryOrderItemsSharedCollection = deliveryOrderItems));

    this.companyContainerService
      .query()
      .pipe(map((res: HttpResponse<ICompanyContainer[]>) => res.body ?? []))
      .pipe(
        map((companyContainers: ICompanyContainer[]) =>
          this.companyContainerService.addCompanyContainerToCollectionIfMissing(
            companyContainers,
            this.editForm.get('companyContainer')!.value
          )
        )
      )
      .subscribe((companyContainers: ICompanyContainer[]) => (this.companyContainersSharedCollection = companyContainers));
  }

  protected createFromForm(): IDeliveryContainer {
    return {
      ...new DeliveryContainer(),
      id: this.editForm.get(['id'])!.value,
      supplierRFIDTag: this.editForm.get(['supplierRFIDTag'])!.value,
      deliveryOrderItem: this.editForm.get(['deliveryOrderItem'])!.value,
      companyContainer: this.editForm.get(['companyContainer'])!.value,
    };
  }
}
