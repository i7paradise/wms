import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeliveryItemProduct } from '../delivery-item-product.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DeliveryItemProductService } from '../service/delivery-item-product.service';
import { DeliveryItemProductDeleteDialogComponent } from '../delete/delivery-item-product-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-delivery-item-product',
  templateUrl: './delivery-item-product.component.html',
})
export class DeliveryItemProductComponent implements OnInit {
  deliveryItemProducts: IDeliveryItemProduct[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected deliveryItemProductService: DeliveryItemProductService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.deliveryItemProducts = [];
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

    this.deliveryItemProductService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IDeliveryItemProduct[]>) => {
          this.isLoading = false;
          this.paginateDeliveryItemProducts(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.deliveryItemProducts = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDeliveryItemProduct): number {
    return item.id!;
  }

  delete(deliveryItemProduct: IDeliveryItemProduct): void {
    const modalRef = this.modalService.open(DeliveryItemProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deliveryItemProduct = deliveryItemProduct;
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

  protected paginateDeliveryItemProducts(data: IDeliveryItemProduct[] | null, headers: HttpHeaders): void {
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
        this.deliveryItemProducts.push(d);
      }
    }
  }
}
