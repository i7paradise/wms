import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliveryOrder } from '../delivery-order.model';
import { DeliveryOrderService } from '../service/delivery-order.service';

@Component({
  templateUrl: './delivery-order-delete-dialog.component.html',
})
export class DeliveryOrderDeleteDialogComponent {
  deliveryOrder?: IDeliveryOrder;

  constructor(protected deliveryOrderService: DeliveryOrderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliveryOrderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
