import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderItemProduct } from '../order-item-product.model';
import { OrderItemProductService } from '../service/order-item-product.service';

@Component({
  templateUrl: './order-item-product-delete-dialog.component.html',
})
export class OrderItemProductDeleteDialogComponent {
  orderItemProduct?: IOrderItemProduct;

  constructor(protected orderItemProductService: OrderItemProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderItemProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
