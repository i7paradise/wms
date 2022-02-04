import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OrderItemDeleteDialogComponent } from 'app/entities/order-item/delete/order-item-delete-dialog.component';
import { IOrderItem } from 'app/entities/order-item/order-item.model';

@Component({
  selector: 'jhi-reception-items',
  templateUrl: './reception-items.component.html',
  styleUrls: ['./reception-items.component.scss'],
})
export class ReceptionItemsComponent {
  @Input() orderItems?: IOrderItem[] | null;

  constructor(private modalService: NgbModal) {
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
}
