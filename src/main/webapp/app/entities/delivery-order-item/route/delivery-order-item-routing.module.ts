import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeliveryOrderItemComponent } from '../list/delivery-order-item.component';
import { DeliveryOrderItemDetailComponent } from '../detail/delivery-order-item-detail.component';
import { DeliveryOrderItemUpdateComponent } from '../update/delivery-order-item-update.component';
import { DeliveryOrderItemRoutingResolveService } from './delivery-order-item-routing-resolve.service';

const deliveryOrderItemRoute: Routes = [
  {
    path: '',
    component: DeliveryOrderItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeliveryOrderItemDetailComponent,
    resolve: {
      deliveryOrderItem: DeliveryOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeliveryOrderItemUpdateComponent,
    resolve: {
      deliveryOrderItem: DeliveryOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeliveryOrderItemUpdateComponent,
    resolve: {
      deliveryOrderItem: DeliveryOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deliveryOrderItemRoute)],
  exports: [RouterModule],
})
export class DeliveryOrderItemRoutingModule {}
