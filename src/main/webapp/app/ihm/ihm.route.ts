import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReceptionDetailComponent } from './reception/reception-detail/reception-detail.component';
import { ReceptionComponent } from './reception/reception.component';
import { ReceptionRoutingResolveService } from './reception/service/reception-routing-resolve.service';
import { ReaderDetailComponent } from './rfid/reader/reader-detail/reader-detail.component';
import { ReaderRoutingResolveService } from './rfid/reader/service/reader-routing-resolve.service';
import { ReaderComponent } from './rfid/reader/reader.component';

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
  {
    path: 'reader',
    component: ReaderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'reader/:id',
    resolve: {
      order: ReaderRoutingResolveService,
    },
    component: ReaderDetailComponent,
    canActivate: [UserRouteAccessService],
  },
];
