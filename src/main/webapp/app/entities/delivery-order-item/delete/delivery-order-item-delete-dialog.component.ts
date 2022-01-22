import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliveryOrderItem } from '../delivery-order-item.model';
import { DeliveryOrderItemService } from '../service/delivery-order-item.service';

@Component({
  templateUrl: './delivery-order-item-delete-dialog.component.html',
})
export class DeliveryOrderItemDeleteDialogComponent {
  deliveryOrderItem?: IDeliveryOrderItem;

  constructor(protected deliveryOrderItemService: DeliveryOrderItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliveryOrderItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
