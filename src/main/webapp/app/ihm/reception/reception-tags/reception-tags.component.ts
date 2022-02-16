import { Component } from '@angular/core';
import { IOrderContainer } from 'app/entities/order-container/order-container.model';
import { IOrderItem, OrderItem } from 'app/entities/order-item/order-item.model';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { ScannerService } from 'app/ihm/scanner/scanner.service';
import { TagsList } from 'app/ihm/model/tags-list.model';
import { ReceptionService } from '../service/reception.service';
import { UiService } from '../service/ui.service';

@Component({
  selector: 'jhi-reception-tags',
  templateUrl: './reception-tags.component.html',
  styleUrls: ['./reception-tags.component.scss'],
})
export class ReceptionTagsComponent {
  orderItem!: OrderItem;
  rfidAntenna!: IUHFRFIDAntenna;
  
  constructor(public uiService: UiService,
    private receptionService: ReceptionService,
    private scannerService: ScannerService
    ) {
    uiService.onSetOrderItem()
      .subscribe((orderItem: IOrderItem) => {
        this.orderItem = orderItem;
        receptionService.findOrderContainers(orderItem)
          .subscribe((list: IOrderContainer[]) => this.orderItem.orderContainers = list);
      });
    uiService.onChangeRFIDAntenna().subscribe((value: IUHFRFIDAntenna) => this.rfidAntenna = value);
  }
  
  getOrderContainers(): IOrderContainer[] {
    return this.orderItem.orderContainers ?? [];
  }

  remainsContainerToScan(): number {
    return ((this.orderItem.containersCount ?? 0) - this.getOrderContainers().length);
  }

  scanContainer(): void {
    console.warn('scanning container');
    this.scannerService.scanWithDialog(this.rfidAntenna, (tags: TagsList) => {
      this.receptionService.createOrderContainersWithTags(this.orderItem, tags);
    });
  }

  scanPackages(container: IOrderContainer): void {
    console.warn('scanning scanPackages for', container);
    this.scannerService.scanWithDialog(this.rfidAntenna, (tags: TagsList) => {
      this.receptionService.createOrderItemProducts(container, tags);
    });  
  }
  
  delete(container: IOrderContainer): void {
    console.warn('clear scan for ' , container);
    // TEMP code

    this.receptionService.deleteContainer(container)
      .subscribe((deleted) => {
        if (deleted) {
            this.orderItem.orderContainers = this.getOrderContainers().filter(e => e !== container);
          }
        }
      );
  }

}
