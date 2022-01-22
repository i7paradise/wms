import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompanyProductComponent } from '../list/company-product.component';
import { CompanyProductDetailComponent } from '../detail/company-product-detail.component';
import { CompanyProductUpdateComponent } from '../update/company-product-update.component';
import { CompanyProductRoutingResolveService } from './company-product-routing-resolve.service';

const companyProductRoute: Routes = [
  {
    path: '',
    component: CompanyProductComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompanyProductDetailComponent,
    resolve: {
      companyProduct: CompanyProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompanyProductUpdateComponent,
    resolve: {
      companyProduct: CompanyProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompanyProductUpdateComponent,
    resolve: {
      companyProduct: CompanyProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(companyProductRoute)],
  exports: [RouterModule],
})
export class CompanyProductRoutingModule {}
