import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CompanyProductService } from 'app/entities/company-product/service/company-product.service';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { OrderItemUpdateComponent } from 'app/entities/order-item/update/order-item-update.component';
import { OrderService } from 'app/entities/order/service/order.service';
import { ReceptionService } from '../service/reception.service';

@Component({
  selector: 'jhi-reception-item-update',
  templateUrl: './reception-item-update.component.html',
  styleUrls: ['./reception-item-update.component.scss']
})
export class ReceptionItemUpdateComponent extends OrderItemUpdateComponent {

  constructor(protected receptionService: ReceptionService,
    protected orderItemService: OrderItemService,
    protected orderService: OrderService,
    protected companyProductService: CompanyProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder) {
      super(orderItemService, orderService, companyProductService, activatedRoute, fb);
  }

  save(): void {
    this.isSaving = true;
    const orderItem = this.createFromForm();
    this.subscribeToSaveResponse(this.receptionService.updateOrderItem(orderItem));
  }
  
}
