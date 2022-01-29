import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WHLevelComponent } from '../list/wh-level.component';
import { WHLevelDetailComponent } from '../detail/wh-level-detail.component';
import { WHLevelUpdateComponent } from '../update/wh-level-update.component';
import { WHLevelRoutingResolveService } from './wh-level-routing-resolve.service';

const wHLevelRoute: Routes = [
  {
    path: '',
    component: WHLevelComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WHLevelDetailComponent,
    resolve: {
      wHLevel: WHLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WHLevelUpdateComponent,
    resolve: {
      wHLevel: WHLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WHLevelUpdateComponent,
    resolve: {
      wHLevel: WHLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wHLevelRoute)],
  exports: [RouterModule],
})
export class WHLevelRoutingModule {}
