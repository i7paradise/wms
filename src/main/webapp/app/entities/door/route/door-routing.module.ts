import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DoorComponent } from '../list/door.component';
import { DoorDetailComponent } from '../detail/door-detail.component';
import { DoorUpdateComponent } from '../update/door-update.component';
import { DoorRoutingResolveService } from './door-routing-resolve.service';

const doorRoute: Routes = [
  {
    path: '',
    component: DoorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DoorDetailComponent,
    resolve: {
      door: DoorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DoorUpdateComponent,
    resolve: {
      door: DoorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DoorUpdateComponent,
    resolve: {
      door: DoorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(doorRoute)],
  exports: [RouterModule],
})
export class DoorRoutingModule {}
