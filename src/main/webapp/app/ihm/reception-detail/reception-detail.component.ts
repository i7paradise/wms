import { Component, OnInit } from '@angular/core';
import { IDeliveryOrderItem } from 'app/entities/delivery-order-item/delivery-order-item.model';
import { DeliveryOrderDetailComponent } from 'app/entities/delivery-order/detail/delivery-order-detail.component';

@Component({
  selector: 'jhi-reception-detail',
  templateUrl: './reception-detail.component.html',
  styleUrls: ['./reception-detail.component.scss'],
})
export class ReceptionDetailComponent extends DeliveryOrderDetailComponent implements OnInit {
  deliveryOrderItems!: IDeliveryOrderItem[];

  ngOnInit(): void {
    super.ngOnInit();
  }
}
