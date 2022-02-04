import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderItemProduct } from '../order-item-product.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { OrderItemProductService } from '../service/order-item-product.service';
import { OrderItemProductDeleteDialogComponent } from '../delete/order-item-product-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-order-item-product',
  templateUrl: './order-item-product.component.html',
})
export class OrderItemProductComponent implements OnInit {
  orderItemProducts: IOrderItemProduct[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected orderItemProductService: OrderItemProductService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.orderItemProducts = [];
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

    this.orderItemProductService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IOrderItemProduct[]>) => {
          this.isLoading = false;
          this.paginateOrderItemProducts(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.orderItemProducts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrderItemProduct): number {
    return item.id!;
  }

  delete(orderItemProduct: IOrderItemProduct): void {
    const modalRef = this.modalService.open(OrderItemProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderItemProduct = orderItemProduct;
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

  protected paginateOrderItemProducts(data: IOrderItemProduct[] | null, headers: HttpHeaders): void {
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
        this.orderItemProducts.push(d);
      }
    }
  }
}
