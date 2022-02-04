import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderContainer } from '../order-container.model';

@Component({
  selector: 'jhi-order-container-detail',
  templateUrl: './order-container-detail.component.html',
})
export class OrderContainerDetailComponent implements OnInit {
  orderContainer: IOrderContainer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderContainer }) => {
      this.orderContainer = orderContainer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
