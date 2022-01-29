import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWHRow } from '../wh-row.model';
import { WHRowService } from '../service/wh-row.service';

@Component({
  templateUrl: './wh-row-delete-dialog.component.html',
})
export class WHRowDeleteDialogComponent {
  wHRow?: IWHRow;

  constructor(protected wHRowService: WHRowService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wHRowService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
