import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBay } from '../bay.model';

@Component({
  selector: 'jhi-bay-detail',
  templateUrl: './bay-detail.component.html',
})
export class BayDetailComponent implements OnInit {
  bay: IBay | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bay }) => {
      this.bay = bay;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
