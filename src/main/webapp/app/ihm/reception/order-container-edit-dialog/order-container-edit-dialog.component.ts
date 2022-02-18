import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IOrderContainerImpl } from 'app/ihm/model/order-container.impl.model';
import { OrderContainerImplService } from 'app/ihm/service/order-container-impl.service';

@Component({
  selector: 'jhi-order-container-edit-dialog',
  templateUrl: './order-container-edit-dialog.component.html',
  styleUrls: ['./order-container-edit-dialog.component.scss'],
})
export class OrderContainerEditDialogComponent {

  public static readonly DELETED_CONTAINER = 'DELETED_CONTAINER';
  public static readonly DELETED_PACKAGES = 'DELETED_PACKAGES';

  orderContainer!: IOrderContainerImpl;

  constructor(private orderContainerService: OrderContainerImplService, private activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  deletePackages(): void {
    this.orderContainerService.deletePackages(this.orderContainer)
      .subscribe(() => {
        this.activeModal.close(OrderContainerEditDialogComponent.DELETED_PACKAGES);
      });
  }

  confirmDelete(): void {
    this.orderContainerService.delete(this.orderContainer.id!)
      .subscribe(() => this.activeModal.close(OrderContainerEditDialogComponent.DELETED_CONTAINER));
  }
}
