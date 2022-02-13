import { Component } from '@angular/core';
import { IOrderItem, OrderItem } from 'app/entities/order-item/order-item.model';
import { UiService } from '../service/ui.service';

@Component({
  selector: 'jhi-reception-tags',
  templateUrl: './reception-tags.component.html',
  styleUrls: ['./reception-tags.component.scss']
})
export class ReceptionTagsComponent {
  orderItem!: OrderItem;

  constructor(public uiService: UiService) {
    uiService.onSetOrderItem().subscribe((orderItem: IOrderItem) => this.orderItem = orderItem);
  }
}
