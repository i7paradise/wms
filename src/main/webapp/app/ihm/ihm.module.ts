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
import { DoorAntennaSelectorComponent } from './scanner/door-antenna-selector/door-antenna-selector.component';
import { DoorAntennaModule } from 'app/entities/door-antenna/door-antenna.module';

@NgModule({
  declarations: [ReceptionComponent, ReceptionDetailComponent, ReceptionItemsComponent, ReceptionTagsComponent, AddItemComponent, ScannerDialogComponent, DoorAntennaSelectorComponent],
  imports: [CommonModule, SharedModule, RouterModule.forChild(ihmRoute), OrderModule, DoorAntennaModule],
})
export class IhmModule {}
