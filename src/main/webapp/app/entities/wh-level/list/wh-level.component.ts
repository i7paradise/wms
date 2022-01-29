import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWHLevel } from '../wh-level.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { WHLevelService } from '../service/wh-level.service';
import { WHLevelDeleteDialogComponent } from '../delete/wh-level-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-wh-level',
  templateUrl: './wh-level.component.html',
})
export class WHLevelComponent implements OnInit {
  wHLevels: IWHLevel[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected wHLevelService: WHLevelService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.wHLevels = [];
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

    this.wHLevelService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IWHLevel[]>) => {
          this.isLoading = false;
          this.paginateWHLevels(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.wHLevels = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWHLevel): number {
    return item.id!;
  }

  delete(wHLevel: IWHLevel): void {
    const modalRef = this.modalService.open(WHLevelDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.wHLevel = wHLevel;
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

  protected paginateWHLevels(data: IWHLevel[] | null, headers: HttpHeaders): void {
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
        this.wHLevels.push(d);
      }
    }
  }
}
