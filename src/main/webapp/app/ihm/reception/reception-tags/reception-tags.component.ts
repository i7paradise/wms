import { Component, Input } from '@angular/core';
import { OrderItem } from 'app/entities/order-item/order-item.model';

@Component({
  selector: 'jhi-reception-tags',
  templateUrl: './reception-tags.component.html',
  styleUrls: ['./reception-tags.component.scss']
})
export class ReceptionTagsComponent {
  @Input() orderItem: OrderItem | null = null;
}
