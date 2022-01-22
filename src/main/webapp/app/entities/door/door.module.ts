import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DoorComponent } from './list/door.component';
import { DoorDetailComponent } from './detail/door-detail.component';
import { DoorUpdateComponent } from './update/door-update.component';
import { DoorDeleteDialogComponent } from './delete/door-delete-dialog.component';
import { DoorRoutingModule } from './route/door-routing.module';

@NgModule({
  imports: [SharedModule, DoorRoutingModule],
  declarations: [DoorComponent, DoorDetailComponent, DoorUpdateComponent, DoorDeleteDialogComponent],
  entryComponents: [DoorDeleteDialogComponent],
})
export class DoorModule {}
