import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { IDoorAntenna } from 'app/entities/door-antenna/door-antenna.model';
import { DoorAntennaService } from 'app/entities/door-antenna/service/door-antenna.service';
import { map } from 'rxjs';

@Component({
  selector: 'jhi-door-antenna-selector',
  templateUrl: './door-antenna-selector.component.html',
  styleUrls: ['./door-antenna-selector.component.scss']
})
export class DoorAntennaSelectorComponent implements OnInit {

  listDoorAntenna: IDoorAntenna[] = [];

  constructor(private doorAntennaService: DoorAntennaService) {}

  ngOnInit(): void {
    this.doorAntennaService
    .query()
    .pipe(map((res: HttpResponse<IDoorAntenna[]>) => res.body ?? []))
    .subscribe((list: IDoorAntenna[]) => 
      this.listDoorAntenna = list.sort((a, b) => a.door?.name?.localeCompare(b.door?.name ?? '') ?? 0)
    );


    
  }

}
