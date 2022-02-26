import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrderContainerComponent } from './list/order-container.component';
import { OrderContainerDetailComponent } from './detail/order-container-detail.component';
import { OrderContainerUpdateComponent } from './update/order-container-update.component';
import { OrderContainerDeleteDialogComponent } from './delete/order-container-delete-dialog.component';
import { OrderContainerRoutingModule } from './route/order-container-routing.module';

@NgModule({
  imports: [SharedModule, OrderContainerRoutingModule],
  declarations: [
    OrderContainerComponent,
    OrderContainerDetailComponent,
    OrderContainerUpdateComponent,
    OrderContainerDeleteDialogComponent,
  ],
  entryComponents: [OrderContainerDeleteDialogComponent],
})
export class OrderContainerModule {}
