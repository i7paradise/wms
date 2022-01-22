import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeliveryOrderComponent } from '../list/delivery-order.component';
import { DeliveryOrderDetailComponent } from '../detail/delivery-order-detail.component';
import { DeliveryOrderUpdateComponent } from '../update/delivery-order-update.component';
import { DeliveryOrderRoutingResolveService } from './delivery-order-routing-resolve.service';

const deliveryOrderRoute: Routes = [
  {
    path: '',
    component: DeliveryOrderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeliveryOrderDetailComponent,
    resolve: {
      deliveryOrder: DeliveryOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeliveryOrderUpdateComponent,
    resolve: {
      deliveryOrder: DeliveryOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeliveryOrderUpdateComponent,
    resolve: {
      deliveryOrder: DeliveryOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deliveryOrderRoute)],
  exports: [RouterModule],
})
export class DeliveryOrderRoutingModule {}
