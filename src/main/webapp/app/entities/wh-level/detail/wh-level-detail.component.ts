import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWHLevel } from '../wh-level.model';

@Component({
  selector: 'jhi-wh-level-detail',
  templateUrl: './wh-level-detail.component.html',
})
export class WHLevelDetailComponent implements OnInit {
  wHLevel: IWHLevel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wHLevel }) => {
      this.wHLevel = wHLevel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
