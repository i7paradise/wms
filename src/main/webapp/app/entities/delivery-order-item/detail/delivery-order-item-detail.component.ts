import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliveryOrderItem } from '../delivery-order-item.model';

@Component({
  selector: 'jhi-delivery-order-item-detail',
  templateUrl: './delivery-order-item-detail.component.html',
})
export class DeliveryOrderItemDetailComponent implements OnInit {
  deliveryOrderItem: IDeliveryOrderItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryOrderItem }) => {
      this.deliveryOrderItem = deliveryOrderItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
