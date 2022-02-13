import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UHFRFIDReaderDetailComponent } from 'app/entities/uhfrfid-reader/detail/uhfrfid-reader-detail.component';

@Component({
  selector: 'jhi-reader-detail',
  templateUrl: './reader-detail.component.html',
  styleUrls: ['./reader-detail.component.scss']
})
export class ReaderDetailComponent extends UHFRFIDReaderDetailComponent implements OnInit {

  constructor(  protected activatedRoute: ActivatedRoute) {
      super(activatedRoute);
    }

  ngOnInit(): void {
      super.ngOnInit();
  }

}
