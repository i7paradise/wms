import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContainerCategoryComponent } from './list/container-category.component';
import { ContainerCategoryDetailComponent } from './detail/container-category-detail.component';
import { ContainerCategoryUpdateComponent } from './update/container-category-update.component';
import { ContainerCategoryDeleteDialogComponent } from './delete/container-category-delete-dialog.component';
import { ContainerCategoryRoutingModule } from './route/container-category-routing.module';

@NgModule({
  imports: [SharedModule, ContainerCategoryRoutingModule],
  declarations: [
    ContainerCategoryComponent,
    ContainerCategoryDetailComponent,
    ContainerCategoryUpdateComponent,
    ContainerCategoryDeleteDialogComponent,
  ],
  entryComponents: [ContainerCategoryDeleteDialogComponent],
})
export class ContainerCategoryModule {}
