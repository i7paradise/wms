import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompanyProductComponent } from './list/company-product.component';
import { CompanyProductDetailComponent } from './detail/company-product-detail.component';
import { CompanyProductUpdateComponent } from './update/company-product-update.component';
import { CompanyProductDeleteDialogComponent } from './delete/company-product-delete-dialog.component';
import { CompanyProductRoutingModule } from './route/company-product-routing.module';

@NgModule({
  imports: [SharedModule, CompanyProductRoutingModule],
  declarations: [
    CompanyProductComponent,
    CompanyProductDetailComponent,
    CompanyProductUpdateComponent,
    CompanyProductDeleteDialogComponent,
  ],
  entryComponents: [CompanyProductDeleteDialogComponent],
})
export class CompanyProductModule {}
