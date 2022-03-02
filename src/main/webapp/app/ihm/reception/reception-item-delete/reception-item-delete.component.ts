import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { OrderItemDeleteDialogComponent } from 'app/entities/order-item/delete/order-item-delete-dialog.component';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { OrderContainerImplService } from 'app/ihm/service/order-container-impl.service';

@Component({
  templateUrl: './reception-item-delete.component.html',
})
export class ReceptionItemDeleteComponent extends OrderItemDeleteDialogComponent implements OnInit {
  constructor(
    protected orderItemService: OrderItemService,
    protected activeModal: NgbActiveModal,
    private orderContainerService: OrderContainerImplService
  ) {
    super(orderItemService, activeModal);
  }

  ngOnInit(): void {
    if (this.orderItem && !this.orderItem.orderContainers) {
      this.orderContainerService.findOrderContainers(this.orderItem).subscribe(list => (this.orderItem!.orderContainers = list));
    }
  }

  canDelete(): boolean {
    if (this.orderItem?.orderContainers) {
      return this.orderItem.orderContainers.length === 0;
    }
    return false;
  }

  confirmDelete(): void {
    if (!this.orderItem) {
      return;
    }
    this.orderItemService.delete(this.orderItem.id!).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
