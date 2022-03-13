import { Component, Input } from '@angular/core';
import { IOrderContainer } from 'app/entities/order-container/order-container.model';
import { IOrderItem, OrderItem } from 'app/entities/order-item/order-item.model';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { ScannerService } from 'app/ihm/scanner/scanner.service';
import { TagsList } from 'app/ihm/model/tags-list.model';
import { UiService } from '../service/ui.service';
import { OrderContainerImplService } from 'app/ihm/service/order-container-impl.service';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';
import { AreaType } from 'app/entities/enumerations/area-type.model';
import { IOrderContainerImpl } from 'app/ihm/model/order-container.impl.model';
import { OrderContainerEditDialogShippingComponent } from '../order-container-edit-dialog/order-container-edit-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import MultiSelectTable from 'app/ihm/tools/multi-select-table';
import { Arrays } from 'app/ihm/tools/helper';
import { IOrder } from 'app/entities/order/order.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

@Component({
  selector: 'jhi-shipping-tags',
  templateUrl: './shipping-tags.component.html',
  styleUrls: ['./shipping-tags.component.scss'],
})
export class ShippingTagsComponent {
  @Input() order: IOrder | null = null;
  orderItem!: OrderItem;
  rfidAntenna!: IUHFRFIDAntenna;
  readonly shippingAreaType = AreaType.SHIPPING;
  readonly multiSelect = new MultiSelectTable<IOrderContainer>(e => e.id!, false);

  constructor(
    public uiService: UiService,
    private scannerService: ScannerService,
    private orderContainerService: OrderContainerImplService,
    private modalService: NgbModal
  ) {
    uiService.onSetOrderItem().subscribe((orderItem: IOrderItem) => {
      this.orderItem = orderItem;
      orderContainerService.findOrderContainers(orderItem).subscribe((list: IOrderContainer[]) => (this.orderItem.orderContainers = list));
    });
    uiService.onChangeRFIDAntenna().subscribe((value: IUHFRFIDAntenna) => (this.rfidAntenna = value));
  }

  canEdit(): boolean {
    return this.orderItem.status !== OrderItemStatus.COMPLETED;
  }

  canDeleteSomeOrderContainers(): boolean {
    return this.canEdit() && this.getOrderContainers().filter(this.canDelete).length > 0;
  }

  canDelete(container: IOrderContainerImpl): boolean {
    return (!container.countProducts || container.countProducts === 0) && Arrays.isEmpty(container.orderItemProducts);
  }

  getOrderContainers(): IOrderContainer[] {
    return this.orderItem.orderContainers ?? [];
  }

  remainsContainerToScan(): number {
    return (this.orderItem.containersCount ?? 0) - this.getOrderContainers().length;
  }

  scanContainer(): void {
    this.scannerService.scanWithDialog(this.rfidAntenna, 1, (tags: TagsList) => {
      this.orderContainerService.createOrderContainersWithTags(this.orderItem, tags)
        .subscribe(() => this.orderToNextStatus());
    });
  }

  openEditDialog(container: IOrderContainerImpl): void {
    const modalRef = this.modalService.open(OrderContainerEditDialogShippingComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.listOrderContainer = [container];
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      this.onDeleteDialog(reason, [container]);
    });
  }

  deleteSelected(): void {
    const selectedContainers = this.getOrderContainers().filter(e => this.multiSelect.isSelected(e));
    console.warn('selectedContainers', selectedContainers);
    const modalRef = this.modalService.open(OrderContainerEditDialogShippingComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.listOrderContainer = selectedContainers;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      this.onDeleteDialog(reason, selectedContainers);
    });
  }

  private onDeleteDialog(reason: string, listContainers: IOrderContainerImpl[]): void {
    if (reason === OrderContainerEditDialogShippingComponent.DELETED_CONTAINER) {
      this.orderItem.orderContainers = this.getOrderContainers().filter(e => !listContainers.includes(e));
    }
    if (reason === OrderContainerEditDialogShippingComponent.DELETED_PACKAGES) {
      listContainers.forEach(container => {
        container.countProducts = undefined;
        container.orderItemProducts = undefined;
      });
    }
  }

  private orderToNextStatus(): void {
    if (this.order && this.order.status === OrderStatus.DRAFT) {
      this.order.status = OrderStatus.IN_PROGRESS;
    }
  }
}
