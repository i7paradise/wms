import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUHFRFIDAntenna } from '../uhfrfid-antenna.model';
import { UHFRFIDAntennaService } from '../service/uhfrfid-antenna.service';

@Component({
  templateUrl: './uhfrfid-antenna-delete-dialog.component.html',
})
export class UHFRFIDAntennaDeleteDialogComponent {
  uHFRFIDAntenna?: IUHFRFIDAntenna;

  constructor(protected uHFRFIDAntennaService: UHFRFIDAntennaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.uHFRFIDAntennaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
