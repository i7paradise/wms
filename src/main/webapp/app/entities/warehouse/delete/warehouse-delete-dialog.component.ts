import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWarehouse } from '../warehouse.model';
import { WarehouseService } from '../service/warehouse.service';

@Component({
  templateUrl: './warehouse-delete-dialog.component.html',
})
export class WarehouseDeleteDialogComponent {
  warehouse?: IWarehouse;

  constructor(protected warehouseService: WarehouseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.warehouseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
