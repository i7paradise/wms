import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliveryOrder } from '../delivery-order.model';

@Component({
  selector: 'jhi-delivery-order-detail',
  templateUrl: './delivery-order-detail.component.html',
})
export class DeliveryOrderDetailComponent implements OnInit {
  deliveryOrder: IDeliveryOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryOrder }) => {
      this.deliveryOrder = deliveryOrder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
