import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UHFRFIDAntennaComponent } from './list/uhfrfid-antenna.component';
import { UHFRFIDAntennaDetailComponent } from './detail/uhfrfid-antenna-detail.component';
import { UHFRFIDAntennaUpdateComponent } from './update/uhfrfid-antenna-update.component';
import { UHFRFIDAntennaDeleteDialogComponent } from './delete/uhfrfid-antenna-delete-dialog.component';
import { UHFRFIDAntennaRoutingModule } from './route/uhfrfid-antenna-routing.module';

@NgModule({
  imports: [SharedModule, UHFRFIDAntennaRoutingModule],
  declarations: [
    UHFRFIDAntennaComponent,
    UHFRFIDAntennaDetailComponent,
    UHFRFIDAntennaUpdateComponent,
    UHFRFIDAntennaDeleteDialogComponent,
  ],
  entryComponents: [UHFRFIDAntennaDeleteDialogComponent],
})
export class UHFRFIDAntennaModule {}
