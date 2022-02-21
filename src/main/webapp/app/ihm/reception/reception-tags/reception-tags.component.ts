import { Component } from '@angular/core';
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
import { OrderContainerEditDialogComponent } from '../order-container-edit-dialog/order-container-edit-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { toJSDate } from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-calendar';

@Component({
  selector: 'jhi-reception-tags',
  templateUrl: './reception-tags.component.html',
  styleUrls: ['./reception-tags.component.scss'],
})
export class ReceptionTagsComponent {
  orderItem!: OrderItem;
  rfidAntenna!: IUHFRFIDAntenna;
  canEdit = true;
  readonly shippingAreaType = AreaType.SHIPPING;

  multiselect = false;
  readonly selectedIds = new Set<number>();

  constructor(
    public uiService: UiService,
    private scannerService: ScannerService,
    private orderContainerService: OrderContainerImplService,
    private modalService: NgbModal
  ) {
    uiService.onSetOrderItem().subscribe((orderItem: IOrderItem) => {
      this.orderItem = orderItem;
      this.canEdit = this.evaluateCanEdit();
      orderContainerService.findOrderContainers(orderItem).subscribe((list: IOrderContainer[]) => (this.orderItem.orderContainers = list));
    });
    uiService.onChangeRFIDAntenna().subscribe((value: IUHFRFIDAntenna) => (this.rfidAntenna = value));
  }

  getOrderContainers(): IOrderContainer[] {
    return this.orderItem.orderContainers ?? [];
  }

  remainsContainerToScan(): number {
    return (this.orderItem.containersCount ?? 0) - this.getOrderContainers().length;
  }

  scanContainer(): void {
    this.scannerService.scanWithDialog(this.rfidAntenna, (tags: TagsList) => {
      this.orderContainerService.createOrderContainersWithTags(this.orderItem, tags);
    });
  }

  openEditDialog(container: IOrderContainerImpl): void {
    const modalRef = this.modalService.open(OrderContainerEditDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderContainer = container;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === OrderContainerEditDialogComponent.DELETED_CONTAINER) {
        this.orderItem.orderContainers = this.getOrderContainers().filter(e => e !== container);
      }
      if (reason === OrderContainerEditDialogComponent.DELETED_PACKAGES) {
        container.countProducts = undefined;
        container.orderItemProducts = undefined;
      }
    });
  }

  onChangeMultiselect(): void {
    this.multiselect = !this.multiselect;
    if (!this.multiselect) {
      this.selectedIds.clear();
    }
    console.warn('ssssssssssssssssssssss', this.multiselect);
  }

  select(container: IOrderContainer): void {
    const id = container.id!;
    if (this.selectedIds.has(id)) {
      this.selectedIds.delete(id);
    } else {
      this.selectedIds.add(id);
    }
  }

  isSelected(container: IOrderContainer): boolean {
    return this.selectedIds.has(container.id!);
  }

  deleteSelected(): void {
    console.warn('ssssssssssssssssssssss', this.selectedIds);
  }

  private evaluateCanEdit(): boolean {
    return this.orderItem.status === OrderItemStatus.IN_PROGRESS;
  }
}
