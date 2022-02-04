import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrderContainerComponent } from '../list/order-container.component';
import { OrderContainerDetailComponent } from '../detail/order-container-detail.component';
import { OrderContainerUpdateComponent } from '../update/order-container-update.component';
import { OrderContainerRoutingResolveService } from './order-container-routing-resolve.service';

const orderContainerRoute: Routes = [
  {
    path: '',
    component: OrderContainerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrderContainerDetailComponent,
    resolve: {
      orderContainer: OrderContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrderContainerUpdateComponent,
    resolve: {
      orderContainer: OrderContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrderContainerUpdateComponent,
    resolve: {
      orderContainer: OrderContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orderContainerRoute)],
  exports: [RouterModule],
})
export class OrderContainerRoutingModule {}
