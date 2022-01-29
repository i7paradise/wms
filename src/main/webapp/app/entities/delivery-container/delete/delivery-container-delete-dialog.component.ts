import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliveryContainer } from '../delivery-container.model';
import { DeliveryContainerService } from '../service/delivery-container.service';

@Component({
  templateUrl: './delivery-container-delete-dialog.component.html',
})
export class DeliveryContainerDeleteDialogComponent {
  deliveryContainer?: IDeliveryContainer;

  constructor(protected deliveryContainerService: DeliveryContainerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deliveryContainerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
