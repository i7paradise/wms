import { Component, Input, OnInit } from '@angular/core';
import { LocationComponent } from 'app/entities/location/list/location.component';
import { ILocation } from 'app/entities/location/location.model';

@Component({
  selector: 'jhi-locations',
  templateUrl: './locations.component.html',
  styleUrls: ['./locations.component.scss']
})
export class LocationsComponent extends LocationComponent implements OnInit {
  @Input() location!: ILocation | null;
  
  ngOnInit(): void {
    this.locations = this.location?.locations ?? [];
  }

  reset(): void {
    // TODO
    // this.uHFRFIDAntennas = this.uHFRFIDAntennas.sort((a,b) => a.name?.localeCompare(b.name));
  }

}
