import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompanyContainer } from '../company-container.model';
import { CompanyContainerService } from '../service/company-container.service';

@Component({
  templateUrl: './company-container-delete-dialog.component.html',
})
export class CompanyContainerDeleteDialogComponent {
  companyContainer?: ICompanyContainer;

  constructor(protected companyContainerService: CompanyContainerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.companyContainerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
