import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompanyContainerComponent } from './list/company-container.component';
import { CompanyContainerDetailComponent } from './detail/company-container-detail.component';
import { CompanyContainerUpdateComponent } from './update/company-container-update.component';
import { CompanyContainerDeleteDialogComponent } from './delete/company-container-delete-dialog.component';
import { CompanyContainerRoutingModule } from './route/company-container-routing.module';

@NgModule({
  imports: [SharedModule, CompanyContainerRoutingModule],
  declarations: [
    CompanyContainerComponent,
    CompanyContainerDetailComponent,
    CompanyContainerUpdateComponent,
    CompanyContainerDeleteDialogComponent,
  ],
  entryComponents: [CompanyContainerDeleteDialogComponent],
})
export class CompanyContainerModule {}
