import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReceptionDetailComponent } from './reception-detail/reception-detail.component';
import { ReceptionComponent } from './reception/reception.component';
import { ReceptionRoutingResolveService } from './service/reception-routing-resolve.service';

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
];
