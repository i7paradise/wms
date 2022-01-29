import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUHFRFIDReader } from '../uhfrfid-reader.model';

@Component({
  selector: 'jhi-uhfrfid-reader-detail',
  templateUrl: './uhfrfid-reader-detail.component.html',
})
export class UHFRFIDReaderDetailComponent implements OnInit {
  uHFRFIDReader: IUHFRFIDReader | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uHFRFIDReader }) => {
      this.uHFRFIDReader = uHFRFIDReader;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
