import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContainerCategory } from '../container-category.model';

@Component({
  selector: 'jhi-container-category-detail',
  templateUrl: './container-category-detail.component.html',
})
export class ContainerCategoryDetailComponent implements OnInit {
  containerCategory: IContainerCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ containerCategory }) => {
      this.containerCategory = containerCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
