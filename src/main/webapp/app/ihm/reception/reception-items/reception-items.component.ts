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

  orderItemStatusValues = Object.keys(OrderItemStatus);
  compganyProductsCollection: ICompanyProduct[] = [];

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
        this.addCompanyProductToCollection(orderItem.compganyProduct);
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
      compganyProduct: [null, [Validators.required]],
    });
  }

  private loadAddOrderElementOptions(): void {
    if (this.compganyProductsCollection.length === 0) {
      const existCompanyProducts = this.orderItems.map(e => e.compganyProduct).map(e => e?.id);

      this.companyProductService
        .query()
        .pipe(map((res: HttpResponse<ICompanyProduct[]>) => res.body ?? []))
        .pipe(map((companyProducts: ICompanyProduct[]) => companyProducts.filter(e => existCompanyProducts.indexOf(e.id) === -1)))
        .pipe(
          map((companyProducts: ICompanyProduct[]) =>
            this.companyProductService.addCompanyProductToCollectionIfMissing(companyProducts, this.editForm.get('compganyProduct')!.value)
          )
        )
        .subscribe((companyProducts: ICompanyProduct[]) => {
          this.compganyProductsCollection = companyProducts;
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
      this.removeCompanyProductFromCollection(orderItem.compganyProduct);
    });
  }

  private createFromForm(): IOrderItem {
    return {
      ...new OrderItem(),
      quantity: this.editForm.get(['quantity'])!.value,
      status: this.editForm.get(['status'])!.value,
      compganyProduct: this.editForm.get(['compganyProduct'])!.value,
      order: this.order,
    };
  }

  private addCompanyProductToCollection(compganyProduct?: ICompanyProduct | null): void {
    if (!compganyProduct) {
      return;
    }
    this.compganyProductsCollection.push(compganyProduct);
    this.sortCompanyProduct();
  }
  private removeCompanyProductFromCollection(compganyProduct?: ICompanyProduct | null): void {
    if (!compganyProduct) {
      return;
    }
    this.compganyProductsCollection = this.compganyProductsCollection.filter(e => compganyProduct.id !== e.id);
    this.sortCompanyProduct();
  }

  private sortCompanyProduct(): void {
    this.compganyProductsCollection = this.compganyProductsCollection.sort((a, b) => a.sku?.localeCompare(b.sku ?? '') ?? 0);
  }
}
