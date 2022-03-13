import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IOrderContainerImpl } from 'app/ihm/model/order-container.impl.model';
import { OrderContainerImplService } from 'app/ihm/service/order-container-impl.service';

@Component({
  selector: 'jhi-order-container-edit-dialog',
  templateUrl: './order-container-edit-dialog.component.html',
  styleUrls: ['./order-container-edit-dialog.component.scss'],
})
export class OrderContainerEditDialogShippingComponent {

  public static readonly DELETED_CONTAINER = 'DELETED_CONTAINER';
  public static readonly DELETED_PACKAGES = 'DELETED_PACKAGES';

  listOrderContainer!: IOrderContainerImpl[];

  constructor(private orderContainerService: OrderContainerImplService, private activeModal: NgbActiveModal) {}

  ids = (): string => this.listOrderContainer.map(e => e.id!.toString()).reduce((a,b) => a + " , " + b);

  cancel(): void {
    this.activeModal.dismiss();
  }

  deletePackages(): void {
    this.orderContainerService.deletePackages(this.listOrderContainer)
      .subscribe(() => {
        this.activeModal.close(OrderContainerEditDialogShippingComponent.DELETED_PACKAGES);
      });
  }

  confirmDelete(): void {
    this.orderContainerService.deleteList(this.listOrderContainer)
      .subscribe(() => this.activeModal.close(OrderContainerEditDialogShippingComponent.DELETED_CONTAINER));
  }
}
