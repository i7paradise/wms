import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContainerComponent } from './list/container.component';
import { ContainerDetailComponent } from './detail/container-detail.component';
import { ContainerUpdateComponent } from './update/container-update.component';
import { ContainerDeleteDialogComponent } from './delete/container-delete-dialog.component';
import { ContainerRoutingModule } from './route/container-routing.module';

@NgModule({
  imports: [SharedModule, ContainerRoutingModule],
  declarations: [ContainerComponent, ContainerDetailComponent, ContainerUpdateComponent, ContainerDeleteDialogComponent],
  entryComponents: [ContainerDeleteDialogComponent],
})
export class ContainerModule {}
