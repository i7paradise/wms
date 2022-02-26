import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderComponent } from 'app/entities/order/list/order.component';
import { OrderService } from 'app/entities/order/service/order.service';
import { ReceptionService } from './service/reception.service';

@Component({
  selector: 'jhi-reception',
  templateUrl: './reception.component.html',
  styleUrls: ['./reception.component.scss'],
})
export class ReceptionComponent extends OrderComponent implements OnInit {
  constructor(
    private receptionService: ReceptionService,
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

    this.receptionService
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
