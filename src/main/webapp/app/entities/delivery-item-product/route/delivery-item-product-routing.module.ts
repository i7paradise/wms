import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeliveryItemProductComponent } from '../list/delivery-item-product.component';
import { DeliveryItemProductDetailComponent } from '../detail/delivery-item-product-detail.component';
import { DeliveryItemProductUpdateComponent } from '../update/delivery-item-product-update.component';
import { DeliveryItemProductRoutingResolveService } from './delivery-item-product-routing-resolve.service';

const deliveryItemProductRoute: Routes = [
  {
    path: '',
    component: DeliveryItemProductComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeliveryItemProductDetailComponent,
    resolve: {
      deliveryItemProduct: DeliveryItemProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeliveryItemProductUpdateComponent,
    resolve: {
      deliveryItemProduct: DeliveryItemProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeliveryItemProductUpdateComponent,
    resolve: {
      deliveryItemProduct: DeliveryItemProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deliveryItemProductRoute)],
  exports: [RouterModule],
})
export class DeliveryItemProductRoutingModule {}
