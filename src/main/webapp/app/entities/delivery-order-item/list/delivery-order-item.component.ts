import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliveryOrderItem } from '../delivery-order-item.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DeliveryOrderItemService } from '../service/delivery-order-item.service';
import { DeliveryOrderItemDeleteDialogComponent } from '../delete/delivery-order-item-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-delivery-order-item',
  templateUrl: './delivery-order-item.component.html',
})
export class DeliveryOrderItemComponent implements OnInit {
  deliveryOrderItems: IDeliveryOrderItem[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected deliveryOrderItemService: DeliveryOrderItemService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.deliveryOrderItems = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.deliveryOrderItemService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IDeliveryOrderItem[]>) => {
          this.isLoading = false;
          this.paginateDeliveryOrderItems(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.deliveryOrderItems = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDeliveryOrderItem): number {
    return item.id!;
  }

  delete(deliveryOrderItem: IDeliveryOrderItem): void {
    const modalRef = this.modalService.open(DeliveryOrderItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deliveryOrderItem = deliveryOrderItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDeliveryOrderItems(data: IDeliveryOrderItem[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.deliveryOrderItems.push(d);
      }
    }
  }
}
