import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UHFRFIDAntennaComponent } from '../list/uhfrfid-antenna.component';
import { UHFRFIDAntennaDetailComponent } from '../detail/uhfrfid-antenna-detail.component';
import { UHFRFIDAntennaUpdateComponent } from '../update/uhfrfid-antenna-update.component';
import { UHFRFIDAntennaRoutingResolveService } from './uhfrfid-antenna-routing-resolve.service';

const uHFRFIDAntennaRoute: Routes = [
  {
    path: '',
    component: UHFRFIDAntennaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UHFRFIDAntennaDetailComponent,
    resolve: {
      uHFRFIDAntenna: UHFRFIDAntennaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UHFRFIDAntennaUpdateComponent,
    resolve: {
      uHFRFIDAntenna: UHFRFIDAntennaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UHFRFIDAntennaUpdateComponent,
    resolve: {
      uHFRFIDAntenna: UHFRFIDAntennaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(uHFRFIDAntennaRoute)],
  exports: [RouterModule],
})
export class UHFRFIDAntennaRoutingModule {}
