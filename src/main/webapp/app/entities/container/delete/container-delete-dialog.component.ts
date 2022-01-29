import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContainer } from '../container.model';
import { ContainerService } from '../service/container.service';

@Component({
  templateUrl: './container-delete-dialog.component.html',
})
export class ContainerDeleteDialogComponent {
  container?: IContainer;

  constructor(protected containerService: ContainerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.containerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
