import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { IDeliveryOrder } from 'app/entities/delivery-order/delivery-order.model';
import { DeliveryOrderComponent } from 'app/entities/delivery-order/list/delivery-order.component';
import { DeliveryOrderService } from 'app/entities/delivery-order/service/delivery-order.service';
import { ReceptionService } from '../service/reception.service';

@Component({
  selector: 'jhi-reception',
  templateUrl: './reception.component.html',
  styleUrls: ['./reception.component.scss'],
})
export class ReceptionComponent extends DeliveryOrderComponent implements OnInit {
  constructor(
    private receptionService: ReceptionService,
    protected deliveryOrderService: DeliveryOrderService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    super(deliveryOrderService, modalService, parseLinks);
  }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;

    this.receptionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IDeliveryOrder[]>) => {
          this.isLoading = false;
          this.paginateDeliveryOrders(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }
}
