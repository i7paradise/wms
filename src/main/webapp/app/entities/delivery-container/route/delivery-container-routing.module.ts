import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeliveryContainerComponent } from '../list/delivery-container.component';
import { DeliveryContainerDetailComponent } from '../detail/delivery-container-detail.component';
import { DeliveryContainerUpdateComponent } from '../update/delivery-container-update.component';
import { DeliveryContainerRoutingResolveService } from './delivery-container-routing-resolve.service';

const deliveryContainerRoute: Routes = [
  {
    path: '',
    component: DeliveryContainerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeliveryContainerDetailComponent,
    resolve: {
      deliveryContainer: DeliveryContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeliveryContainerUpdateComponent,
    resolve: {
      deliveryContainer: DeliveryContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeliveryContainerUpdateComponent,
    resolve: {
      deliveryContainer: DeliveryContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deliveryContainerRoute)],
  exports: [RouterModule],
})
export class DeliveryContainerRoutingModule {}
