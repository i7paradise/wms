import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrderItemProductComponent } from '../list/order-item-product.component';
import { OrderItemProductDetailComponent } from '../detail/order-item-product-detail.component';
import { OrderItemProductUpdateComponent } from '../update/order-item-product-update.component';
import { OrderItemProductRoutingResolveService } from './order-item-product-routing-resolve.service';

const orderItemProductRoute: Routes = [
  {
    path: '',
    component: OrderItemProductComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrderItemProductDetailComponent,
    resolve: {
      orderItemProduct: OrderItemProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrderItemProductUpdateComponent,
    resolve: {
      orderItemProduct: OrderItemProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrderItemProductUpdateComponent,
    resolve: {
      orderItemProduct: OrderItemProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orderItemProductRoute)],
  exports: [RouterModule],
})
export class OrderItemProductRoutingModule {}
