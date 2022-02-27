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

@NgModule({
  declarations: [ReceptionComponent, ReceptionDetailComponent, ReceptionItemsComponent, ReceptionTagsComponent, AddItemComponent,
    ScannerDialogComponent, AntennaSelectorComponent, ScanPackagesComponent,
    OrderContainerEditDialogComponent, ReceptionItemUpdateComponent,
    ReaderComponent,
    ReaderDetailComponent,
    AntennasComponent],
  imports: [CommonModule, SharedModule, RouterModule.forChild(ihmRoute), OrderModule, UHFRFIDAntennaModule]
})
export class IhmModule {}
