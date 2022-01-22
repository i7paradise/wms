import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeliveryOrderItemComponent } from './list/delivery-order-item.component';
import { DeliveryOrderItemDetailComponent } from './detail/delivery-order-item-detail.component';
import { DeliveryOrderItemUpdateComponent } from './update/delivery-order-item-update.component';
import { DeliveryOrderItemDeleteDialogComponent } from './delete/delivery-order-item-delete-dialog.component';
import { DeliveryOrderItemRoutingModule } from './route/delivery-order-item-routing.module';

@NgModule({
  imports: [SharedModule, DeliveryOrderItemRoutingModule],
  declarations: [
    DeliveryOrderItemComponent,
    DeliveryOrderItemDetailComponent,
    DeliveryOrderItemUpdateComponent,
    DeliveryOrderItemDeleteDialogComponent,
  ],
  entryComponents: [DeliveryOrderItemDeleteDialogComponent],
})
export class DeliveryOrderItemModule {}
