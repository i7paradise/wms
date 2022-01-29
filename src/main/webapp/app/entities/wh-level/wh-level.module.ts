import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WHLevelComponent } from './list/wh-level.component';
import { WHLevelDetailComponent } from './detail/wh-level-detail.component';
import { WHLevelUpdateComponent } from './update/wh-level-update.component';
import { WHLevelDeleteDialogComponent } from './delete/wh-level-delete-dialog.component';
import { WHLevelRoutingModule } from './route/wh-level-routing.module';

@NgModule({
  imports: [SharedModule, WHLevelRoutingModule],
  declarations: [WHLevelComponent, WHLevelDetailComponent, WHLevelUpdateComponent, WHLevelDeleteDialogComponent],
  entryComponents: [WHLevelDeleteDialogComponent],
})
export class WHLevelModule {}
