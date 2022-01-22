import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DoorAntennaComponent } from './list/door-antenna.component';
import { DoorAntennaDetailComponent } from './detail/door-antenna-detail.component';
import { DoorAntennaUpdateComponent } from './update/door-antenna-update.component';
import { DoorAntennaDeleteDialogComponent } from './delete/door-antenna-delete-dialog.component';
import { DoorAntennaRoutingModule } from './route/door-antenna-routing.module';

@NgModule({
  imports: [SharedModule, DoorAntennaRoutingModule],
  declarations: [DoorAntennaComponent, DoorAntennaDetailComponent, DoorAntennaUpdateComponent, DoorAntennaDeleteDialogComponent],
  entryComponents: [DoorAntennaDeleteDialogComponent],
})
export class DoorAntennaModule {}
