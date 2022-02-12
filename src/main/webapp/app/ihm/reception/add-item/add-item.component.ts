import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Validators } from '@angular/forms';
import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { IOrderItem, OrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemUpdateComponent } from 'app/entities/order-item/update/order-item-update.component';
import { Order } from 'app/entities/order/order.model';
import { finalize, map, Observable } from 'rxjs';

@Component({
  selector: 'jhi-add-item',
  templateUrl: './add-item.component.html',
  styleUrls: ['./add-item.component.scss']
})
export class AddItemComponent extends OrderItemUpdateComponent implements OnInit {
  @Input() order!: Order;
  @Output() createdOrderItemEvt = new EventEmitter<IOrderItem>();
  addMode = false;

  ngOnInit(): void {
      this.addMode = false;
  }

  canAddItems(): boolean {
    // TODO check order status
    return true;
  }

  startAddMode(): void {
    this.addMode = true;
    this.loadAddOrderElementOptions();
    this.editForm = this.fb.group({
      quantity: [null, [Validators.required, Validators.min(0)]],
      status: [null, [Validators.required]],
      containersCount: [null, [Validators.min(0)]],
      productsPerContainerCount: [null, [Validators.min(0)]],
      companyProduct: [],
    });
  }

  cancel(): void {
    this.addMode = false;
  }

  protected createFromForm(): IOrderItem {
    return {
      ...new OrderItem(),
      quantity: this.editForm.get(['quantity'])!.value,
      status: this.editForm.get(['status'])!.value,
      containersCount: this.editForm.get(['containersCount'])!.value,
      productsPerContainerCount: this.editForm.get(['productsPerContainerCount'])!.value,
      companyProduct: this.editForm.get(['companyProduct'])!.value,
      order: {
        id: this.order.id
      }
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize()))
          .subscribe({
            next: (response: HttpResponse<IOrderItem>) => this.onCreateSuccess(response.body),
            error: () => this.onSaveError(),
          });
  }

  private onCreateSuccess(orderItem: IOrderItem | null): void {
    console.warn('calling onCreateSuccess' , orderItem);
    this.addMode = false;
    if (orderItem) {
      console.warn('event out' , orderItem);
      this.createdOrderItemEvt.emit(orderItem);
    }
  }

  private loadAddOrderElementOptions(): void {
    if (this.companyProductsSharedCollection.length === 0) {
      this.companyProductService
        .query()
        .pipe(map((res: HttpResponse<ICompanyProduct[]>) => res.body ?? []))
        .pipe(
          map((companyProducts: ICompanyProduct[]) =>
            this.companyProductService.addCompanyProductToCollectionIfMissing(companyProducts, this.editForm.get('companyProduct')!.value)
          )
        )
        .subscribe((companyProducts: ICompanyProduct[]) => 
          this.companyProductsSharedCollection = companyProducts.sort((a, b) => a.sku?.localeCompare(b.sku ?? '') ?? 0));
    }
  }
  
}
