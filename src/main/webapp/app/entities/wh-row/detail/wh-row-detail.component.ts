import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWHRow } from '../wh-row.model';

@Component({
  selector: 'jhi-wh-row-detail',
  templateUrl: './wh-row-detail.component.html',
})
export class WHRowDetailComponent implements OnInit {
  wHRow: IWHRow | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wHRow }) => {
      this.wHRow = wHRow;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
