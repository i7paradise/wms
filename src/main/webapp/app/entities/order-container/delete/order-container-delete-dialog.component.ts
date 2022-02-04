import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderContainer } from '../order-container.model';
import { OrderContainerService } from '../service/order-container.service';

@Component({
  templateUrl: './order-container-delete-dialog.component.html',
})
export class OrderContainerDeleteDialogComponent {
  orderContainer?: IOrderContainer;

  constructor(protected orderContainerService: OrderContainerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderContainerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
