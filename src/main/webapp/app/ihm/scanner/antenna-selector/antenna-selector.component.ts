import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UHFRFIDAntennaService } from 'app/entities/uhfrfid-antenna/service/uhfrfid-antenna.service';
import { UHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { UiService } from 'app/ihm/reception/service/ui.service';
import { map } from 'rxjs';

@Component({
  selector: 'jhi-antenna-selector',
  templateUrl: './antenna-selector.component.html',
  styleUrls: ['./antenna-selector.component.scss']
})
export class AntennaSelectorComponent implements OnInit {

  listAntenna: UHFRFIDAntenna[] = [];

  constructor(private rfidAntennaService: UHFRFIDAntennaService,
    public uiService: UiService
    ) { }

  ngOnInit(): void {
    // TODO query the correct antennas
    this.rfidAntennaService
      .query()
      .pipe(map((res: HttpResponse<UHFRFIDAntenna[]>) => res.body ?? []))
      .subscribe((list: UHFRFIDAntenna[]) =>
        this.listAntenna = list.sort((a, b) => a.id ?? 0 - (b.id ?? 0))
      );
  }

  onChange(id: string): void {
    const selected = this.listAntenna.filter(e => e.id === parseInt(id, 10))[0];
    this.uiService.setRFIDAntenna(selected);
  }

}
