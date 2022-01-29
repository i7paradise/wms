import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeliveryContainerComponent } from './list/delivery-container.component';
import { DeliveryContainerDetailComponent } from './detail/delivery-container-detail.component';
import { DeliveryContainerUpdateComponent } from './update/delivery-container-update.component';
import { DeliveryContainerDeleteDialogComponent } from './delete/delivery-container-delete-dialog.component';
import { DeliveryContainerRoutingModule } from './route/delivery-container-routing.module';

@NgModule({
  imports: [SharedModule, DeliveryContainerRoutingModule],
  declarations: [
    DeliveryContainerComponent,
    DeliveryContainerDetailComponent,
    DeliveryContainerUpdateComponent,
    DeliveryContainerDeleteDialogComponent,
  ],
  entryComponents: [DeliveryContainerDeleteDialogComponent],
})
export class DeliveryContainerModule {}
