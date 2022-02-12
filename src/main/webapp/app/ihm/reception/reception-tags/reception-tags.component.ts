import { Component } from '@angular/core';
import { IOrderContainer, OrderContainer } from 'app/entities/order-container/order-container.model';
import { IOrderItem, OrderItem } from 'app/entities/order-item/order-item.model';
import { UiService } from '../service/ui.service';

@Component({
  selector: 'jhi-reception-tags',
  templateUrl: './reception-tags.component.html',
  styleUrls: ['./reception-tags.component.scss'],
})
export class ReceptionTagsComponent {
  orderItem!: OrderItem;
  
  constructor(public uiService: UiService) {
    uiService.onSetOrderItem().subscribe((orderItem: IOrderItem) => this.orderItem = orderItem);
  }
  
  getOrderContainers(): IOrderContainer[] {
    return this.orderItem.orderContainers ?? [];
  }

  remainsContainerToScan(): number {
    return (this.orderItem.containersCount ?? 0) - this.getOrderContainers().length;
  }

  scanContainer(): void {
    console.warn('scanning container');
    if(!this.orderItem.orderContainers) {
      this.orderItem.orderContainers = [];
    }
    // TEMP code
    const container = {...new OrderContainer(),
      supplierRFIDTag: Math.random().toString(36).replace(/[^a-z]+/g, '').substr(0, 5)
    };
    this.orderItem.orderContainers.unshift(container);
  }

  scanPackages(container: IOrderContainer): void {
    console.warn('scanning scanPackages for', container);
    // container.
  }
  
  clearScan(container: IOrderContainer): void {
    console.warn('clear scan for ' , container);
    // TEMP code
    this.orderItem.orderContainers = this.getOrderContainers().filter(e => e !== container);
  }


}
