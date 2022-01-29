import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUHFRFIDAntenna } from '../uhfrfid-antenna.model';

@Component({
  selector: 'jhi-uhfrfid-antenna-detail',
  templateUrl: './uhfrfid-antenna-detail.component.html',
})
export class UHFRFIDAntennaDetailComponent implements OnInit {
  uHFRFIDAntenna: IUHFRFIDAntenna | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uHFRFIDAntenna }) => {
      this.uHFRFIDAntenna = uHFRFIDAntenna;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
