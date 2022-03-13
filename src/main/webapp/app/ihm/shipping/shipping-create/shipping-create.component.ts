/* eslint-disable @angular-eslint/no-empty-lifecycle-method */
/* eslint-disable @angular-eslint/use-lifecycle-interface */
import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CompanyService } from 'app/entities/company/service/company.service';
import { OrderService } from 'app/entities/order/service/order.service';
import { OrderUpdateComponent } from 'app/entities/order/update/order-update.component';
import { ShippingService } from '../service/shipping.service';

@Component({
  selector: 'jhi-shipping-create',
  templateUrl: './shipping-create.component.html',
  styleUrls: ['./shipping-create.component.scss']
})
export class ShippingCreateComponent extends OrderUpdateComponent {

  constructor(private shippingService: ShippingService,
    protected orderService: OrderService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder) {
    super(orderService, companyService, activatedRoute, fb);
   }

  ngOnInit(): void {
    // do nothing, prevent load Order from super.ngOnInit
  }

}
