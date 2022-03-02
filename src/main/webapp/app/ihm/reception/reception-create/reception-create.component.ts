/* eslint-disable @angular-eslint/no-empty-lifecycle-method */
/* eslint-disable @angular-eslint/use-lifecycle-interface */
import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CompanyService } from 'app/entities/company/service/company.service';
import { OrderService } from 'app/entities/order/service/order.service';
import { OrderUpdateComponent } from 'app/entities/order/update/order-update.component';
import { ReceptionService } from '../service/reception.service';

@Component({
  selector: 'jhi-reception-create',
  templateUrl: './reception-create.component.html',
  styleUrls: ['./reception-create.component.scss']
})
export class ReceptionCreateComponent extends OrderUpdateComponent {

  constructor(private receptionService: ReceptionService,
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
