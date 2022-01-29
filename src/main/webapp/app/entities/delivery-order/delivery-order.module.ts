import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeliveryOrderComponent } from './list/delivery-order.component';
import { DeliveryOrderDetailComponent } from './detail/delivery-order-detail.component';
import { DeliveryOrderUpdateComponent } from './update/delivery-order-update.component';
import { DeliveryOrderDeleteDialogComponent } from './delete/delivery-order-delete-dialog.component';
import { DeliveryOrderRoutingModule } from './route/delivery-order-routing.module';

@NgModule({
  imports: [SharedModule, DeliveryOrderRoutingModule],
  declarations: [DeliveryOrderComponent, DeliveryOrderDetailComponent, DeliveryOrderUpdateComponent, DeliveryOrderDeleteDialogComponent],
  entryComponents: [DeliveryOrderDeleteDialogComponent],
})
export class DeliveryOrderModule {}
