import { Component, OnInit } from '@angular/core';
import { DeliveryOrderComponent } from 'app/entities/delivery-order/list/delivery-order.component';

@Component({
  selector: 'jhi-reception',
  templateUrl: './reception.component.html',
  styleUrls: ['./reception.component.scss'],
})
export class ReceptionComponent extends DeliveryOrderComponent implements OnInit {
  ngOnInit(): void {
    super.ngOnInit();
  }
}
