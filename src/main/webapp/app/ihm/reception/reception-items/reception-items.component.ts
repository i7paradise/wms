import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { CompanyProductService } from 'app/entities/company-product/service/company-product.service';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';
import { OrderItemDeleteDialogComponent } from 'app/entities/order-item/delete/order-item-delete-dialog.component';
import { IOrderItem, OrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { Order } from 'app/entities/order/order.model';
import { map, Observable } from 'rxjs';

@Component({
  selector: 'jhi-reception-items',
  templateUrl: './reception-items.component.html',
  styleUrls: ['./reception-items.component.scss'],
})
export class ReceptionItemsComponent implements OnInit {
  @Input() order!: Order;
  orderItems: IOrderItem[] = [];
  isSaving = false;
  addMode = false;
  editForm!: FormGroup;
  orderItemForTags: OrderItem | null = null;

  orderItemStatusValues = Object.keys(OrderItemStatus);
  companyProductsCollection: ICompanyProduct[] = [];

  constructor(
    private modalService: NgbModal,
    private companyProductService: CompanyProductService,
    private orderItemService: OrderItemService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.orderItems = this.order.orderItems ?? [];
  }

  delete(orderItem: IOrderItem): void {
    const modalRef = this.modalService.open(OrderItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderItem = orderItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.orderItems = this.orderItems.filter(e => orderItem !== e);
        this.addCompanyProductToCollection(orderItem.companyProduct);
      }
    });
  }

  save(): void {
    this.isSaving = true;
    const orderItem = this.createFromForm();
    this.subscribeToSaveResponse(this.orderItemService.create(orderItem));
    this.addMode = false;
  }

  cancel(): void {
    this.addMode = false;
  }

  trackCompanyProductById(index: number, item: ICompanyProduct): number {
    return item.id!;
  }

  canAddItems(): boolean {
    return !this.addMode;
  }

  startAddMode(): void {
    this.addMode = true;
    this.loadAddOrderElementOptions();
    this.editForm = this.fb.group({
      quantity: [null, [Validators.required, Validators.min(0)]],
      status: [null, [Validators.required]],
      companyProduct: [null, [Validators.required]],
    });
  }

  defineOrderTags(orderItem: OrderItem): void {
    this.orderItemForTags = this.orderItemForTags === orderItem ? null : orderItem;
  }

  private loadAddOrderElementOptions(): void {
    if (this.companyProductsCollection.length === 0) {
      const existCompanyProducts = this.orderItems.map(e => e.companyProduct).map(e => e?.id);

      this.companyProductService
        .query()
        .pipe(map((res: HttpResponse<ICompanyProduct[]>) => res.body ?? []))
        .pipe(map((companyProducts: ICompanyProduct[]) => companyProducts.filter(e => existCompanyProducts.indexOf(e.id) === -1)))
        .pipe(
          map((companyProducts: ICompanyProduct[]) =>
            this.companyProductService.addCompanyProductToCollectionIfMissing(companyProducts, this.editForm.get('companyProduct')!.value)
          )
        )
        .subscribe((companyProducts: ICompanyProduct[]) => {
          this.companyProductsCollection = companyProducts;
          this.sortCompanyProduct();
        });
    }
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<IOrderItem>>): void {
    this.isSaving = false;
    result.pipe(map(response => response.body ?? null)).subscribe(orderItem => {
      if (orderItem === null) {
        return;
      }
      this.orderItems.unshift(orderItem);
      this.removeCompanyProductFromCollection(orderItem.companyProduct);
    });
  }

  private createFromForm(): IOrderItem {
    return {
      ...new OrderItem(),
      quantity: this.editForm.get(['quantity'])!.value,
      status: this.editForm.get(['status'])!.value,
      companyProduct: this.editForm.get(['companyProduct'])!.value,
      order: this.order,
    };
  }

  private addCompanyProductToCollection(companyProduct?: ICompanyProduct | null): void {
    if (!companyProduct) {
      return;
    }
    this.companyProductsCollection.push(companyProduct);
    this.sortCompanyProduct();
  }
  private removeCompanyProductFromCollection(companyProduct?: ICompanyProduct | null): void {
    if (!companyProduct) {
      return;
    }
    this.companyProductsCollection = this.companyProductsCollection.filter(e => companyProduct.id !== e.id);
    this.sortCompanyProduct();
  }

  private sortCompanyProduct(): void {
    this.companyProductsCollection = this.companyProductsCollection.sort((a, b) => a.sku?.localeCompare(b.sku ?? '') ?? 0);
  }
}
