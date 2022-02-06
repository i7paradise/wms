import { HttpResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ICompanyProduct } from 'app/entities/company-product/company-product.model';
import { CompanyProductService } from 'app/entities/company-product/service/company-product.service';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';
import { OrderItemDeleteDialogComponent } from 'app/entities/order-item/delete/order-item-delete-dialog.component';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { map } from 'rxjs';

@Component({
  selector: 'jhi-reception-items',
  templateUrl: './reception-items.component.html',
  styleUrls: ['./reception-items.component.scss'],
})
export class ReceptionItemsComponent {
  @Input() orderItems?: IOrderItem[] | null;
  isSaving = false;
  addMode = false;

  orderItemStatusValues = Object.keys(OrderItemStatus);
  compganyProductsCollection: ICompanyProduct[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required, Validators.min(0)]],
    status: [null, [Validators.required]],
    compganyProduct: [],
    order: [],
  });

  constructor(private modalService: NgbModal,
    private companyProductService: CompanyProductService,
    protected fb: FormBuilder
    ) {
    this.orderItems = [];
  }

  delete(orderItem: IOrderItem): void {
    const modalRef = this.modalService.open(OrderItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderItem = orderItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.orderItems = this.orderItems?.filter(e => orderItem !== e);
      }
    });
  }

  save(): void {
    // TODO
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
  }

  private loadAddOrderElementOptions(): void {
    // TODO test diff with get with no filter!!
    if (this.compganyProductsCollection.length === 0) {
      this.companyProductService
      .query({ filter: 'orderitem-is-null' })
      .pipe(map((res: HttpResponse<ICompanyProduct[]>) => res.body ?? []))
      .pipe(
        map((companyProducts: ICompanyProduct[]) =>
          this.companyProductService.addCompanyProductToCollectionIfMissing(companyProducts, this.editForm.get('compganyProduct')!.value)
        )
      )
      .subscribe((companyProducts: ICompanyProduct[]) => (this.compganyProductsCollection = companyProducts));

    }
  }
}
