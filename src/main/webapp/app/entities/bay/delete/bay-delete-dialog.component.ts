import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBay } from '../bay.model';
import { BayService } from '../service/bay.service';

@Component({
  templateUrl: './bay-delete-dialog.component.html',
})
export class BayDeleteDialogComponent {
  bay?: IBay;

  constructor(protected bayService: BayService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bayService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
