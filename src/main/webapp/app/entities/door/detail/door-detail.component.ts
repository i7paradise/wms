import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDoor } from '../door.model';

@Component({
  selector: 'jhi-door-detail',
  templateUrl: './door-detail.component.html',
})
export class DoorDetailComponent implements OnInit {
  door: IDoor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ door }) => {
      this.door = door;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
