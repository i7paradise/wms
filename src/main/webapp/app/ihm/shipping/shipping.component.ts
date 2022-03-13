import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderComponent } from 'app/entities/order/list/order.component';
import { OrderService } from 'app/entities/order/service/order.service';
import { ShippingService } from './service/shipping.service';

@Component({
  selector: 'jhi-shipping',
  templateUrl: './shipping.component.html',
  styleUrls: ['./shipping.component.scss'],
})
export class ShippingComponent extends OrderComponent implements OnInit {
  constructor(
    private shippingService: ShippingService,
    protected orderService: OrderService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    super(orderService, modalService, parseLinks);
  }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;

    this.shippingService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IOrder[]>) => {
          this.isLoading = false;
          this.paginateOrders(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }
}
