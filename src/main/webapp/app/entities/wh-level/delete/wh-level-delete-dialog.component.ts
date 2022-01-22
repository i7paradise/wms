import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWHLevel } from '../wh-level.model';
import { WHLevelService } from '../service/wh-level.service';

@Component({
  templateUrl: './wh-level-delete-dialog.component.html',
})
export class WHLevelDeleteDialogComponent {
  wHLevel?: IWHLevel;

  constructor(protected wHLevelService: WHLevelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wHLevelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
