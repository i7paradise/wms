import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReceptionComponent } from './reception/reception.component';
import { SharedModule } from 'app/shared/shared.module';
import { OrderModule } from 'app/entities/order/order.module';
import { ihmRoute } from './ihm.route';
import { RouterModule } from '@angular/router';
import { ReceptionDetailComponent } from './reception/reception-detail/reception-detail.component';
import { ReceptionItemsComponent } from './reception/reception-items/reception-items.component';
import { ReceptionTagsComponent } from './reception/reception-tags/reception-tags.component';
import { AddItemComponent } from './reception/add-item/add-item.component';
import { ScannerDialogComponent } from './scanner/scanner-dialog/scanner-dialog.component';
import { AntennaSelectorComponent } from './scanner/antenna-selector/antenna-selector.component';
import { UHFRFIDAntennaModule } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.module';
import { ScanPackagesComponent } from './reception/scan-packages/scan-packages.component';
import { OrderContainerEditDialogComponent } from './reception/order-container-edit-dialog/order-container-edit-dialog.component';
import { ReceptionItemUpdateComponent } from './reception/reception-item-update/reception-item-update.component';
import { ReaderComponent } from './rfid/reader/reader.component';
import { ReaderDetailComponent } from './rfid/reader/reader-detail/reader-detail.component';
import { AntennasComponent } from './rfid/reader/antennas/antennas.component';
import { XspsWarehouseComponent } from './xsps-warehouse/xsps-warehouse.component';
import { XspsWarehouseDetailComponent } from './xsps-warehouse/xsps-warehouse-detail/xsps-warehouse-detail.component';
import { LocationsComponent } from './xsps-warehouse/locations/locations.component';
import { ReceptionCreateComponent } from './reception/reception-create/reception-create.component';
import { ReceptionItemDeleteComponent } from './reception/reception-item-delete/reception-item-delete.component';

import { ShippingComponent } from './shipping/shipping.component';
import { ShippingDetailComponent } from './shipping/shipping-detail/shipping-detail.component';
import { ShippingItemsComponent } from './shipping/shipping-items/shipping-items.component';
import { ShippingTagsComponent } from './shipping/shipping-tags/shipping-tags.component';

import { AddItemShippingComponent } from './shipping/add-item/add-item.component';
import { ScanPackagesShippingComponent } from './shipping/scan-packages/scan-packages.component';
import { OrderContainerEditDialogShippingComponent } from './shipping/order-container-edit-dialog/order-container-edit-dialog.component';


import { ShippingItemUpdateComponent } from './shipping/shipping-item-update/shipping-item-update.component';
import { ShippingCreateComponent } from './shipping/shipping-create/shipping-create.component';
import { ShippingItemDeleteComponent } from './shipping/shipping-item-delete/shipping-item-delete.component';


@NgModule({
  declarations: [ReceptionComponent, ReceptionDetailComponent, ReceptionItemsComponent, ReceptionTagsComponent, AddItemComponent,
    ScannerDialogComponent, AntennaSelectorComponent, ScanPackagesComponent,
    OrderContainerEditDialogComponent, ReceptionItemUpdateComponent,
    ReaderComponent,
    ReaderDetailComponent,
    AntennasComponent,
    XspsWarehouseComponent,
    XspsWarehouseDetailComponent,
    LocationsComponent,
    ReceptionCreateComponent,
    ReceptionItemDeleteComponent,
    ShippingComponent, ShippingDetailComponent, ShippingItemsComponent, ShippingTagsComponent,
    AddItemShippingComponent, ScanPackagesShippingComponent, OrderContainerEditDialogShippingComponent,
    ShippingItemUpdateComponent,
    ShippingCreateComponent,
    ShippingItemDeleteComponent],
  imports: [CommonModule, SharedModule, RouterModule.forChild(ihmRoute), OrderModule, UHFRFIDAntennaModule]
})
export class IhmModule {}
