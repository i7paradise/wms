import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliveryContainer } from '../delivery-container.model';

@Component({
  selector: 'jhi-delivery-container-detail',
  templateUrl: './delivery-container-detail.component.html',
})
export class DeliveryContainerDetailComponent implements OnInit {
  deliveryContainer: IDeliveryContainer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryContainer }) => {
      this.deliveryContainer = deliveryContainer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
