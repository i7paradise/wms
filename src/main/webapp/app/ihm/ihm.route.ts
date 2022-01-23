import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeliveryOrderRoutingResolveService } from 'app/entities/delivery-order/route/delivery-order-routing-resolve.service';
import { ReceptionDetailComponent } from './reception-detail/reception-detail.component';
import { ReceptionComponent } from './reception/reception.component';

export const ihmRoute: Routes = [
  {
    path: 'reception',
    component: ReceptionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'reception/:id',
    resolve: {
      deliveryOrder: DeliveryOrderRoutingResolveService,
    },
    component: ReceptionDetailComponent,
    canActivate: [UserRouteAccessService],
  },
];
