import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BayComponent } from './list/bay.component';
import { BayDetailComponent } from './detail/bay-detail.component';
import { BayUpdateComponent } from './update/bay-update.component';
import { BayDeleteDialogComponent } from './delete/bay-delete-dialog.component';
import { BayRoutingModule } from './route/bay-routing.module';

@NgModule({
  imports: [SharedModule, BayRoutingModule],
  declarations: [BayComponent, BayDetailComponent, BayUpdateComponent, BayDeleteDialogComponent],
  entryComponents: [BayDeleteDialogComponent],
})
export class BayModule {}
