import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDoorAntenna } from '../door-antenna.model';

@Component({
  selector: 'jhi-door-antenna-detail',
  templateUrl: './door-antenna-detail.component.html',
})
export class DoorAntennaDetailComponent implements OnInit {
  doorAntenna: IDoorAntenna | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ doorAntenna }) => {
      this.doorAntenna = doorAntenna;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
