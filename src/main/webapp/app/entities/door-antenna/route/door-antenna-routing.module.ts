import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DoorAntennaComponent } from '../list/door-antenna.component';
import { DoorAntennaDetailComponent } from '../detail/door-antenna-detail.component';
import { DoorAntennaUpdateComponent } from '../update/door-antenna-update.component';
import { DoorAntennaRoutingResolveService } from './door-antenna-routing-resolve.service';

const doorAntennaRoute: Routes = [
  {
    path: '',
    component: DoorAntennaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DoorAntennaDetailComponent,
    resolve: {
      doorAntenna: DoorAntennaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DoorAntennaUpdateComponent,
    resolve: {
      doorAntenna: DoorAntennaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DoorAntennaUpdateComponent,
    resolve: {
      doorAntenna: DoorAntennaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(doorAntennaRoute)],
  exports: [RouterModule],
})
export class DoorAntennaRoutingModule {}
