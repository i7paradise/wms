import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WHRowComponent } from '../list/wh-row.component';
import { WHRowDetailComponent } from '../detail/wh-row-detail.component';
import { WHRowUpdateComponent } from '../update/wh-row-update.component';
import { WHRowRoutingResolveService } from './wh-row-routing-resolve.service';

const wHRowRoute: Routes = [
  {
    path: '',
    component: WHRowComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WHRowDetailComponent,
    resolve: {
      wHRow: WHRowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WHRowUpdateComponent,
    resolve: {
      wHRow: WHRowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WHRowUpdateComponent,
    resolve: {
      wHRow: WHRowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wHRowRoute)],
  exports: [RouterModule],
})
export class WHRowRoutingModule {}
