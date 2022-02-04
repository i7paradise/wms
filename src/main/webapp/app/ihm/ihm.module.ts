import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReceptionComponent } from './reception/reception.component';
import { SharedModule } from 'app/shared/shared.module';
import { OrderModule } from 'app/entities/order/order.module';
import { ihmRoute } from './ihm.route';
import { RouterModule } from '@angular/router';
import { ReceptionDetailComponent } from './reception-detail/reception-detail.component';
import { ReceptionItemsComponent } from './reception-items/reception-items.component';

@NgModule({
  declarations: [ReceptionComponent, ReceptionDetailComponent, ReceptionItemsComponent],
  imports: [CommonModule, SharedModule, RouterModule.forChild(ihmRoute), OrderModule],
})
export class IhmModule {}
