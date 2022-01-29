import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliveryItemProduct } from '../delivery-item-product.model';
import { DeliveryItemProductService } from '../service/delivery-item-product.service';

@Component({
  templateUrl: './delivery-item-product-delete-dialog.component.html',
})
export class DeliveryItemProductDeleteDialogComponent {
  deliveryItemProduct?: IDeliveryItemProduct;

  constructor(protected deliveryItemProductService: DeliveryItemProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliveryItemProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
