import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UHFRFIDReaderComponent } from '../list/uhfrfid-reader.component';
import { UHFRFIDReaderDetailComponent } from '../detail/uhfrfid-reader-detail.component';
import { UHFRFIDReaderUpdateComponent } from '../update/uhfrfid-reader-update.component';
import { UHFRFIDReaderRoutingResolveService } from './uhfrfid-reader-routing-resolve.service';

const uHFRFIDReaderRoute: Routes = [
  {
    path: '',
    component: UHFRFIDReaderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UHFRFIDReaderDetailComponent,
    resolve: {
      uHFRFIDReader: UHFRFIDReaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UHFRFIDReaderUpdateComponent,
    resolve: {
      uHFRFIDReader: UHFRFIDReaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UHFRFIDReaderUpdateComponent,
    resolve: {
      uHFRFIDReader: UHFRFIDReaderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(uHFRFIDReaderRoute)],
  exports: [RouterModule],
})
export class UHFRFIDReaderRoutingModule {}
