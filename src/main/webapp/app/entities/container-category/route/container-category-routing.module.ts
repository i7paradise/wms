import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContainerCategoryComponent } from '../list/container-category.component';
import { ContainerCategoryDetailComponent } from '../detail/container-category-detail.component';
import { ContainerCategoryUpdateComponent } from '../update/container-category-update.component';
import { ContainerCategoryRoutingResolveService } from './container-category-routing-resolve.service';

const containerCategoryRoute: Routes = [
  {
    path: '',
    component: ContainerCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContainerCategoryDetailComponent,
    resolve: {
      containerCategory: ContainerCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContainerCategoryUpdateComponent,
    resolve: {
      containerCategory: ContainerCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContainerCategoryUpdateComponent,
    resolve: {
      containerCategory: ContainerCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(containerCategoryRoute)],
  exports: [RouterModule],
})
export class ContainerCategoryRoutingModule {}
