import { Component, Input, OnInit } from '@angular/core';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { IOrderContainerImpl } from 'app/ihm/model/order-container.impl.model';
import { TagsList } from 'app/ihm/model/tags-list.model';
import { ScannerService } from 'app/ihm/scanner/scanner.service';
import { OrderContainerImplService } from 'app/ihm/service/order-container-impl.service';
import { Arrays } from 'app/ihm/tools/helper';
import { UiService } from '../service/ui.service';

@Component({
  selector: 'jhi-scan-packages',
  templateUrl: './scan-packages.component.html',
  styleUrls: ['./scan-packages.component.scss']
})
export class ScanPackagesShippingComponent implements OnInit {

  @Input() container!: IOrderContainerImpl;
  rfidAntenna: IUHFRFIDAntenna = this.uiService.getRFIDAntenna();

  constructor(private scannerService: ScannerService,
    private orderContainerService: OrderContainerImplService,
    private uiService: UiService,
  ) { }

  ngOnInit(): void {
    this.uiService.onChangeRFIDAntenna().subscribe((value: IUHFRFIDAntenna) => this.rfidAntenna = value);
  }

  scan(): void {
    const count = this.uiService.getOrderItem().productsPerContainerCount!;
    this.scannerService.scanWithDialog(this.rfidAntenna, count, (tags: TagsList) => {
      this.orderContainerService.createOrderItemProducts(this.container, tags)
        .subscribe(list => {
          this.container.orderItemProducts = list;
          this.replaceInOrderItem(this.container);
          this.orderItemToNextStatus();
          this.updateOrderItemProductQuantity(list.length);
        });
    });
  }

  countProducts(): number | null {
    if (this.container.orderItemProducts && this.container.orderItemProducts.length > 0) {
      return this.container.orderItemProducts.length;
    }
    if (this.container.countProducts! > 0) {
      return this.container.countProducts!;
    }
    return null;
  }

  private replaceInOrderItem(container: IOrderContainerImpl): void {
    const list = this.uiService.getOrderItem().orderContainers;
    if (!list) {
      return;
    }
    const index = list.findIndex(e => e.id === container.id);
    list[index] = container;
  }

  private orderItemToNextStatus(): void {
    const orderItem = this.uiService.getOrderItem();
    if (orderItem.status === OrderItemStatus.COMPLETED) {
      console.warn('@@@@@ was completed @@@');
      return;
    }
    if (orderItem.containersCount !== Arrays.size(orderItem.orderContainers)) {
      return;
    }
    const allPackagesScanned = Arrays.isNotEmpty(
      orderItem.orderContainers?.filter(e => Arrays.isNotEmpty(e.orderItemProducts))
    );
    if (allPackagesScanned) {
      orderItem.status = OrderItemStatus.COMPLETED;
    }
  }

  private updateOrderItemProductQuantity(quantity: number): void {
    const companyProduct = this.uiService.getOrderItem().companyProduct;
    if (!companyProduct) { return; }
    if (!companyProduct.quantity) {
      companyProduct.quantity = 0;
    }
    companyProduct.quantity = companyProduct.quantity + quantity;
    // TODO update in backend
  }

}
