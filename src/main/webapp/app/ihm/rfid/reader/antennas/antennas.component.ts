import { Component, Input, OnInit } from '@angular/core';
import { UHFRFIDAntennaComponent } from 'app/entities/uhfrfid-antenna/list/uhfrfid-antenna.component';
import { IUHFRFIDReader } from 'app/entities/uhfrfid-reader/uhfrfid-reader.model';

@Component({
  selector: 'jhi-antennas',
  templateUrl: './antennas.component.html',
  styleUrls: ['./antennas.component.scss']
})
export class AntennasComponent extends UHFRFIDAntennaComponent implements OnInit {
  @Input() reader!: IUHFRFIDReader | null;
  
  ngOnInit(): void {
    this.uHFRFIDAntennas = this.reader?.uhfRFIDAntennas ?? [];
  }

  reset(): void {
    // TODO
    // this.uHFRFIDAntennas = this.uHFRFIDAntennas.sort((a,b) => a.name?.localeCompare(b.name));
  }
}
