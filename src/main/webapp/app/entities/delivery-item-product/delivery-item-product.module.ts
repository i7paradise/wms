import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeliveryItemProductComponent } from './list/delivery-item-product.component';
import { DeliveryItemProductDetailComponent } from './detail/delivery-item-product-detail.component';
import { DeliveryItemProductUpdateComponent } from './update/delivery-item-product-update.component';
import { DeliveryItemProductDeleteDialogComponent } from './delete/delivery-item-product-delete-dialog.component';
import { DeliveryItemProductRoutingModule } from './route/delivery-item-product-routing.module';

@NgModule({
  imports: [SharedModule, DeliveryItemProductRoutingModule],
  declarations: [
    DeliveryItemProductComponent,
    DeliveryItemProductDetailComponent,
    DeliveryItemProductUpdateComponent,
    DeliveryItemProductDeleteDialogComponent,
  ],
  entryComponents: [DeliveryItemProductDeleteDialogComponent],
})
export class DeliveryItemProductModule {}
