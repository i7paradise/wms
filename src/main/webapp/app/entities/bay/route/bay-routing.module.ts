import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BayComponent } from '../list/bay.component';
import { BayDetailComponent } from '../detail/bay-detail.component';
import { BayUpdateComponent } from '../update/bay-update.component';
import { BayRoutingResolveService } from './bay-routing-resolve.service';

const bayRoute: Routes = [
  {
    path: '',
    component: BayComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BayDetailComponent,
    resolve: {
      bay: BayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BayUpdateComponent,
    resolve: {
      bay: BayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BayUpdateComponent,
    resolve: {
      bay: BayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bayRoute)],
  exports: [RouterModule],
})
export class BayRoutingModule {}
