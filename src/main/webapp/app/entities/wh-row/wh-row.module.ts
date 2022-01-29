import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WHRowComponent } from './list/wh-row.component';
import { WHRowDetailComponent } from './detail/wh-row-detail.component';
import { WHRowUpdateComponent } from './update/wh-row-update.component';
import { WHRowDeleteDialogComponent } from './delete/wh-row-delete-dialog.component';
import { WHRowRoutingModule } from './route/wh-row-routing.module';

@NgModule({
  imports: [SharedModule, WHRowRoutingModule],
  declarations: [WHRowComponent, WHRowDetailComponent, WHRowUpdateComponent, WHRowDeleteDialogComponent],
  entryComponents: [WHRowDeleteDialogComponent],
})
export class WHRowModule {}
