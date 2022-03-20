import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { CompanyProductService } from 'app/entities/company-product/service/company-product.service';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';
import { IOrderItem, OrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { OrderItemUpdateComponent } from 'app/entities/order-item/update/order-item-update.component';
import { Order } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { finalize, map, Observable } from 'rxjs';
import { ShippingService } from '../service/shipping.service';

@Component({
  selector: 'jhi-add-item',
  templateUrl: './add-item.component.html',
  styleUrls: ['./add-item.component.scss'],
})
export class AddItemShippingComponent extends OrderItemUpdateComponent implements OnInit {
  @Input() order!: Order;
  @Output() createdOrderItemEvt = new EventEmitter<IOrderItem>();
  addMode = false;
  private selectedProduct!: ICompanyProduct | null;
  private selectedQuantity!: number;

  constructor(
    protected orderItemService: OrderItemService,
    protected orderService: OrderService,
    protected companyProductService: CompanyProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private shippingService: ShippingService
  ) {
    super(orderItemService, orderService, companyProductService, activatedRoute, fb);
  }

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
      containersCount: [null, [Validators.min(0)]],
      productsPerContainerCount: [null, [Validators.min(0)]],
      companyProduct: [],
    });
  }

  cancel(): void {
    this.addMode = false;
  }

  onChangeSelectedProduct(selectedProduct: ICompanyProduct): void {
    this.selectedProduct = selectedProduct;
    this.autoCompleteCounters();
  }

  onChangeQuantity(quantity: number): void {
    this.selectedQuantity = quantity;
    this.autoCompleteCounters();
  }

  protected createFromForm(): IOrderItem {
    return {
      ...new OrderItem(),
      quantity: this.editForm.get(['quantity'])!.value,
      status: OrderItemStatus.DRAFT,
      containersCount: this.editForm.get(['containersCount'])!.value,
      productsPerContainerCount: this.editForm.get(['productsPerContainerCount'])!.value,
      companyProduct: this.editForm.get(['companyProduct'])!.value,
      order: {
        id: this.order.id,
      },
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: (response: HttpResponse<IOrderItem>) => this.onCreateSuccess(response.body),
      error: () => this.onSaveError(),
    });
  }

  private onCreateSuccess(orderItem: IOrderItem | null): void {
    this.addMode = false;
    this.shippingService.findOrderItem(orderItem?.id as number)
      .subscribe(e => {
        if (e.body) {
          this.createdOrderItemEvt.emit(e.body);
          console.warn('orderItem', e.body);
        }
      });
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
        .subscribe((companyProducts: ICompanyProduct[]) => {
          this.companyProductsSharedCollection = companyProducts.sort((a, b) => a.product?.name?.localeCompare(b.product?.name ?? '') ?? 0);
        });
    }
  }

  private autoCompleteCounters(): void {
    if (!this.selectedProduct || !this.selectedQuantity) {
      return;
    }
    const v = Math.ceil(this.selectedQuantity / this.selectedProduct.containerStockingRatio!);
    
    this.editForm.get(['containersCount'])?.setValue(v);
    this.editForm.get(['productsPerContainerCount'])?.setValue(this.selectedProduct.containerStockingRatio);
  }
}