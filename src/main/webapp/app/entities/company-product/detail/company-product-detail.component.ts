import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompanyProduct } from '../company-product.model';

@Component({
  selector: 'jhi-company-product-detail',
  templateUrl: './company-product-detail.component.html',
})
export class CompanyProductDetailComponent implements OnInit {
  companyProduct: ICompanyProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyProduct }) => {
      this.companyProduct = companyProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
