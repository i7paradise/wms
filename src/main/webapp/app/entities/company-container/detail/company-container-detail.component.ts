import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompanyContainer } from '../company-container.model';

@Component({
  selector: 'jhi-company-container-detail',
  templateUrl: './company-container-detail.component.html',
})
export class CompanyContainerDetailComponent implements OnInit {
  companyContainer: ICompanyContainer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companyContainer }) => {
      this.companyContainer = companyContainer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
