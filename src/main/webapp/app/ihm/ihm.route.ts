import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReceptionDetailComponent } from './reception/reception-detail/reception-detail.component';
import { ReceptionItemUpdateComponent } from './reception/reception-item-update/reception-item-update.component';
import { ReceptionComponent } from './reception/reception.component';
import { ReceptionItemRoutingResolveService } from './reception/service/reception-item-routing-resolve.service';
import { ReceptionRoutingResolveService } from './reception/service/reception-routing-resolve.service';

export const ihmRoute: Routes = [
  {
    path: 'reception',
    component: ReceptionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'reception/:id',
    resolve: {
      order: ReceptionRoutingResolveService,
    },
    component: ReceptionDetailComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'reception-item/:id/edit',
    resolve: {
      orderItem: ReceptionItemRoutingResolveService,
    },
    component: ReceptionItemUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
];
