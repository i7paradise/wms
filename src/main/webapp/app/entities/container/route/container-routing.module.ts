import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContainerComponent } from '../list/container.component';
import { ContainerDetailComponent } from '../detail/container-detail.component';
import { ContainerUpdateComponent } from '../update/container-update.component';
import { ContainerRoutingResolveService } from './container-routing-resolve.service';

const containerRoute: Routes = [
  {
    path: '',
    component: ContainerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContainerDetailComponent,
    resolve: {
      container: ContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContainerUpdateComponent,
    resolve: {
      container: ContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContainerUpdateComponent,
    resolve: {
      container: ContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(containerRoute)],
  exports: [RouterModule],
})
export class ContainerRoutingModule {}
