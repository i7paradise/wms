import { Component, Input, OnInit } from '@angular/core';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { IOrderContainerImpl } from 'app/ihm/model/order-container.impl.model';
import { TagsList } from 'app/ihm/model/tags-list.model';
import { ScannerService } from 'app/ihm/scanner/scanner.service';
import { OrderContainerImplService } from 'app/ihm/service/order-container-impl.service';
import { UiService } from '../service/ui.service';

@Component({
  selector: 'jhi-scan-packages',
  templateUrl: './scan-packages.component.html',
  styleUrls: ['./scan-packages.component.scss']
})
export class ScanPackagesComponent implements OnInit {

  @Input() container!: IOrderContainerImpl;
  rfidAntenna: IUHFRFIDAntenna = this.uiService.getRFIDAntenna();

  constructor(private scannerService: ScannerService,
    private orderContainerService: OrderContainerImplService,
    private uiService: UiService,
  ) { }

  ngOnInit(): void {
    this.uiService.onChangeRFIDAntenna().subscribe((value: IUHFRFIDAntenna) => this.rfidAntenna = value);
  }

  scan(): void {
    this.scannerService.scanWithDialog(this.rfidAntenna, (tags: TagsList) => {
      this.orderContainerService.createOrderItemProducts(this.container, tags)
      .subscribe(list => {
        this.container.orderItemProducts = list
      });
    });
  }

  countProducts(): number | null {
    if (this.container.orderItemProducts && this.container.orderItemProducts.length > 0) {
      return this.container.orderItemProducts.length;
    }
    if (this.container.countProducts! > 0) {
      return this.container.countProducts!;
    }
    return null;
  }

}
