import { Component, OnInit } from '@angular/core';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderDetailComponent } from 'app/entities/order/detail/order-detail.component';

@Component({
  selector: 'jhi-reception-detail',
  templateUrl: './reception-detail.component.html',
  styleUrls: ['./reception-detail.component.scss'],
})
export class ReceptionDetailComponent extends OrderDetailComponent implements OnInit {
  orderItems!: IOrderItem[];

  ngOnInit(): void {
    super.ngOnInit();
  }
}
