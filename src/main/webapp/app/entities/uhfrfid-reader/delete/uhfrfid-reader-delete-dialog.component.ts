import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUHFRFIDReader } from '../uhfrfid-reader.model';
import { UHFRFIDReaderService } from '../service/uhfrfid-reader.service';

@Component({
  templateUrl: './uhfrfid-reader-delete-dialog.component.html',
})
export class UHFRFIDReaderDeleteDialogComponent {
  uHFRFIDReader?: IUHFRFIDReader;

  constructor(protected uHFRFIDReaderService: UHFRFIDReaderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.uHFRFIDReaderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
