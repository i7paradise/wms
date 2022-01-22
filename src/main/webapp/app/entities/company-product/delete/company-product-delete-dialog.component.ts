import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompanyProduct } from '../company-product.model';
import { CompanyProductService } from '../service/company-product.service';

@Component({
  templateUrl: './company-product-delete-dialog.component.html',
})
export class CompanyProductDeleteDialogComponent {
  companyProduct?: ICompanyProduct;

  constructor(protected companyProductService: CompanyProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.companyProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
