import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CompanyProductService } from 'app/entities/company-product/service/company-product.service';
import { OrderItemDeleteDialogComponent } from 'app/entities/order-item/delete/order-item-delete-dialog.component';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { Order } from 'app/entities/order/order.model';

@Component({
  selector: 'jhi-reception-items',
  templateUrl: './reception-items.component.html',
  styleUrls: ['./reception-items.component.scss'],
})
export class ReceptionItemsComponent implements OnInit {
  @Input() order!: Order;
  orderItems: IOrderItem[] = [];
  isSaving = false;

  constructor(
    private modalService: NgbModal,
    private companyProductService: CompanyProductService,
    private orderItemService: OrderItemService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.orderItems = this.order.orderItems ?? [];
  }

  delete(orderItem: IOrderItem): void {
    const modalRef = this.modalService.open(OrderItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
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
}
