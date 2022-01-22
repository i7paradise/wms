import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUHFRFIDAntenna } from '../uhfrfid-antenna.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { UHFRFIDAntennaService } from '../service/uhfrfid-antenna.service';
import { UHFRFIDAntennaDeleteDialogComponent } from '../delete/uhfrfid-antenna-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-uhfrfid-antenna',
  templateUrl: './uhfrfid-antenna.component.html',
})
export class UHFRFIDAntennaComponent implements OnInit {
  uHFRFIDAntennas: IUHFRFIDAntenna[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected uHFRFIDAntennaService: UHFRFIDAntennaService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.uHFRFIDAntennas = [];
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

    this.uHFRFIDAntennaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IUHFRFIDAntenna[]>) => {
          this.isLoading = false;
          this.paginateUHFRFIDAntennas(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.uHFRFIDAntennas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUHFRFIDAntenna): number {
    return item.id!;
  }

  delete(uHFRFIDAntenna: IUHFRFIDAntenna): void {
    const modalRef = this.modalService.open(UHFRFIDAntennaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.uHFRFIDAntenna = uHFRFIDAntenna;
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

  protected paginateUHFRFIDAntennas(data: IUHFRFIDAntenna[] | null, headers: HttpHeaders): void {
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
        this.uHFRFIDAntennas.push(d);
      }
    }
  }
}
