import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDoor } from '../door.model';
import { DoorService } from '../service/door.service';

@Component({
  templateUrl: './door-delete-dialog.component.html',
})
export class DoorDeleteDialogComponent {
  door?: IDoor;

  constructor(protected doorService: DoorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.doorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
