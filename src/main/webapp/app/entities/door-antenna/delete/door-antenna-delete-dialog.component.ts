import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDoorAntenna } from '../door-antenna.model';
import { DoorAntennaService } from '../service/door-antenna.service';

@Component({
  templateUrl: './door-antenna-delete-dialog.component.html',
})
export class DoorAntennaDeleteDialogComponent {
  doorAntenna?: IDoorAntenna;

  constructor(protected doorAntennaService: DoorAntennaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.doorAntennaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
