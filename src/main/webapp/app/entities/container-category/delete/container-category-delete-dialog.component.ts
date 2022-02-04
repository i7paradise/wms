import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContainerCategory } from '../container-category.model';
import { ContainerCategoryService } from '../service/container-category.service';

@Component({
  templateUrl: './container-category-delete-dialog.component.html',
})
export class ContainerCategoryDeleteDialogComponent {
  containerCategory?: IContainerCategory;

  constructor(protected containerCategoryService: ContainerCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.containerCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
