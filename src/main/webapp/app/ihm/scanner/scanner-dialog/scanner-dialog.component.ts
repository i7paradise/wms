import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TagsList } from '../tags-list.model';
import { ScannerService } from '../scanner.service';

@Component({
  selector: 'jhi-scanner-dialog',
  templateUrl: './scanner-dialog.component.html',
  styleUrls: ['./scanner-dialog.component.scss']
})
export class ScannerDialogComponent {

  public static CONFIRM = 'confirm';

  tagsList!: TagsList | null;
  loading = false;

  constructor(protected scannerService: ScannerService,
    protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirm(): void {
    this.activeModal.close(ScannerDialogComponent.CONFIRM);
  }

  rescan(): void {
    this.loading = true;
    this.tagsList = null;
    this.scannerService.scan()
      .subscribe((tagsList: TagsList) => {
        this.tagsList = tagsList;
        this.loading = false;
      });
  }

}
