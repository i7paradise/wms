import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DeliveryOrderItemDeleteDialogComponent } from 'app/entities/delivery-order-item/delete/delivery-order-item-delete-dialog.component';
import { IDeliveryOrderItem } from 'app/entities/delivery-order-item/delivery-order-item.model';

@Component({
  selector: 'jhi-reception-items',
  templateUrl: './reception-items.component.html',
  styleUrls: ['./reception-items.component.scss'],
})
export class ReceptionItemsComponent {
  @Input() deliveryOrderItems?: IDeliveryOrderItem[] | null;

  constructor(private modalService: NgbModal) {
    this.deliveryOrderItems = [];
  }

  delete(deliveryOrderItem: IDeliveryOrderItem): void {
    const modalRef = this.modalService.open(DeliveryOrderItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deliveryOrderItem = deliveryOrderItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.deliveryOrderItems = this.deliveryOrderItems?.filter(e => deliveryOrderItem !== e);
      }
    });
  }
}
