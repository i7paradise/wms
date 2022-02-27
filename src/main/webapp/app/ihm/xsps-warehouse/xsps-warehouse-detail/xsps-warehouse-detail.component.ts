import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WarehouseDetailComponent } from 'app/entities/warehouse/detail/warehouse-detail.component';

@Component({
  selector: 'jhi-xsps-warehouse-detail',
  templateUrl: './xsps-warehouse-detail.component.html',
  styleUrls: ['./xsps-warehouse-detail.component.scss']
})
export class XspsWarehouseDetailComponent extends WarehouseDetailComponent implements OnInit {

  constructor(  protected activatedRoute: ActivatedRoute) {
      super(activatedRoute);
    }

  ngOnInit(): void {
      super.ngOnInit();
  }

}
