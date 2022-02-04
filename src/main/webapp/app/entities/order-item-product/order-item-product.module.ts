import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrderItemProductComponent } from './list/order-item-product.component';
import { OrderItemProductDetailComponent } from './detail/order-item-product-detail.component';
import { OrderItemProductUpdateComponent } from './update/order-item-product-update.component';
import { OrderItemProductDeleteDialogComponent } from './delete/order-item-product-delete-dialog.component';
import { OrderItemProductRoutingModule } from './route/order-item-product-routing.module';

@NgModule({
  imports: [SharedModule, OrderItemProductRoutingModule],
  declarations: [
    OrderItemProductComponent,
    OrderItemProductDetailComponent,
    OrderItemProductUpdateComponent,
    OrderItemProductDeleteDialogComponent,
  ],
  entryComponents: [OrderItemProductDeleteDialogComponent],
})
export class OrderItemProductModule {}
