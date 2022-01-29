import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompanyContainerComponent } from '../list/company-container.component';
import { CompanyContainerDetailComponent } from '../detail/company-container-detail.component';
import { CompanyContainerUpdateComponent } from '../update/company-container-update.component';
import { CompanyContainerRoutingResolveService } from './company-container-routing-resolve.service';

const companyContainerRoute: Routes = [
  {
    path: '',
    component: CompanyContainerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompanyContainerDetailComponent,
    resolve: {
      companyContainer: CompanyContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompanyContainerUpdateComponent,
    resolve: {
      companyContainer: CompanyContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompanyContainerUpdateComponent,
    resolve: {
      companyContainer: CompanyContainerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(companyContainerRoute)],
  exports: [RouterModule],
})
export class CompanyContainerRoutingModule {}
