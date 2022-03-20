import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { Order } from 'app/entities/order/order.model';
import { ShippingItemDeleteComponent } from '../shipping-item-delete/shipping-item-delete.component';
import { UiService } from '../service/ui.service';

@Component({
  selector: 'jhi-shipping-items',
  templateUrl: './shipping-items.component.html',
  styleUrls: ['./shipping-items.component.scss'],
})
export class ShippingItemsComponent implements OnInit {
  @Input() order!: Order;
  orderItems: IOrderItem[] = [];
  isSaving = false;
  readonly orderStatus = OrderStatus;
  readonly orderItemStatus = OrderItemStatus;

  constructor(
    private modalService: NgbModal,
    public uiService: UiService
  ) {}

  ngOnInit(): void {
    this.orderItems = this.order.orderItems ?? [];
  }

  delete(orderItem: IOrderItem): void {
    const modalRef = this.modalService.open(ShippingItemDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderItem = orderItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.orderItems = this.orderItems.filter(e => orderItem !== e);
      }
    });
  }

  onAddOrderItem(orderItem: IOrderItem): void {
    this.orderItems.unshift(orderItem);
  }

  canEdit(orderItem: IOrderItem): boolean {
    return orderItem.status === OrderItemStatus.DRAFT;
  }
}