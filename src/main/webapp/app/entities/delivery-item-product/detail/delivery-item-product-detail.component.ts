import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliveryItemProduct } from '../delivery-item-product.model';

@Component({
  selector: 'jhi-delivery-item-product-detail',
  templateUrl: './delivery-item-product-detail.component.html',
})
export class DeliveryItemProductDetailComponent implements OnInit {
  deliveryItemProduct: IDeliveryItemProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryItemProduct }) => {
      this.deliveryItemProduct = deliveryItemProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
