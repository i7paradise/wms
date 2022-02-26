import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderItemProduct } from '../order-item-product.model';

@Component({
  selector: 'jhi-order-item-product-detail',
  templateUrl: './order-item-product-detail.component.html',
})
export class OrderItemProductDetailComponent implements OnInit {
  orderItemProduct: IOrderItemProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItemProduct }) => {
      this.orderItemProduct = orderItemProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
