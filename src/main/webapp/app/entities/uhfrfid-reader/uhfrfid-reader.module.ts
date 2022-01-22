import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UHFRFIDReaderComponent } from './list/uhfrfid-reader.component';
import { UHFRFIDReaderDetailComponent } from './detail/uhfrfid-reader-detail.component';
import { UHFRFIDReaderUpdateComponent } from './update/uhfrfid-reader-update.component';
import { UHFRFIDReaderDeleteDialogComponent } from './delete/uhfrfid-reader-delete-dialog.component';
import { UHFRFIDReaderRoutingModule } from './route/uhfrfid-reader-routing.module';

@NgModule({
  imports: [SharedModule, UHFRFIDReaderRoutingModule],
  declarations: [UHFRFIDReaderComponent, UHFRFIDReaderDetailComponent, UHFRFIDReaderUpdateComponent, UHFRFIDReaderDeleteDialogComponent],
  entryComponents: [UHFRFIDReaderDeleteDialogComponent],
})
export class UHFRFIDReaderModule {}
