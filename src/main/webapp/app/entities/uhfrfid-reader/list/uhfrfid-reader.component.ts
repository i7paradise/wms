import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUHFRFIDReader } from '../uhfrfid-reader.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { UHFRFIDReaderService } from '../service/uhfrfid-reader.service';
import { UHFRFIDReaderDeleteDialogComponent } from '../delete/uhfrfid-reader-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-uhfrfid-reader',
  templateUrl: './uhfrfid-reader.component.html',
})
export class UHFRFIDReaderComponent implements OnInit {
  uHFRFIDReaders: IUHFRFIDReader[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected uHFRFIDReaderService: UHFRFIDReaderService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.uHFRFIDReaders = [];
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

    this.uHFRFIDReaderService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IUHFRFIDReader[]>) => {
          this.isLoading = false;
          this.paginateUHFRFIDReaders(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.uHFRFIDReaders = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUHFRFIDReader): number {
    return item.id!;
  }

  delete(uHFRFIDReader: IUHFRFIDReader): void {
    const modalRef = this.modalService.open(UHFRFIDReaderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.uHFRFIDReader = uHFRFIDReader;
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

  protected paginateUHFRFIDReaders(data: IUHFRFIDReader[] | null, headers: HttpHeaders): void {
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
        this.uHFRFIDReaders.push(d);
      }
    }
  }
}
