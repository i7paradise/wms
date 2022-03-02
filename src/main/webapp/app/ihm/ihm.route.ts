import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReceptionDetailComponent } from './reception/reception-detail/reception-detail.component';
import { ReceptionItemUpdateComponent } from './reception/reception-item-update/reception-item-update.component';
import { ReceptionItemRoutingResolveService } from './reception/service/reception-item-routing-resolve.service';
import { ReceptionComponent } from './reception/reception.component';
import { ReceptionRoutingResolveService } from './reception/service/reception-routing-resolve.service';
import { ReaderDetailComponent } from './rfid/reader/reader-detail/reader-detail.component';
import { ReaderRoutingResolveService } from './rfid/reader/service/reader-routing-resolve.service';
import { ReaderComponent } from './rfid/reader/reader.component';
import { XspsWarehouseComponent} from './xsps-warehouse/xsps-warehouse.component';
import { XspsWarehouseRoutingResolveService } from './xsps-warehouse/service/xsps-warehouse-routing-resolve.service';
import { XspsWarehouseDetailComponent } from './xsps-warehouse/xsps-warehouse-detail/xsps-warehouse-detail.component';
import { ReceptionCreateComponent } from './reception/reception-create/reception-create.component';

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
    path: 'reception-create',
    component: ReceptionCreateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'reception-item/:id/edit',
    resolve: {
      orderItem: ReceptionItemRoutingResolveService,
    },
    component: ReceptionItemUpdateComponent,
  },
  {
    path: 'reader',
    component: ReaderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'reader/:id',
    resolve: {
      uHFRFIDReader: ReaderRoutingResolveService,
    },
    component: ReaderDetailComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'warehouse',
    component: XspsWarehouseComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'warehouse/:id',
    resolve: {
      warehouse: XspsWarehouseRoutingResolveService,
    },
    component: XspsWarehouseDetailComponent,
    canActivate: [UserRouteAccessService],
  },
];
