import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { DoorAntenna } from 'app/entities/door-antenna/door-antenna.model';
import { map, Observable } from 'rxjs';
import { ScannerDialogComponent } from './scanner-dialog/scanner-dialog.component';
import { TagsList } from './tags-list.model';

@Injectable({
  providedIn: 'root',
})
export class ScannerService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/v1/tags');

  constructor(private applicationConfigService: ApplicationConfigService,
    protected http: HttpClient,
    protected modalService: NgbModal) {}

  scan(doorAntenna: DoorAntenna): Observable<TagsList> {
    const copy = {
      ...new DoorAntenna(),
      id: doorAntenna.id
    };
    return this.http
      .post<TagsList>(this.resourceUrl + '/scan', copy, { observe: 'response' })
      .pipe(map((res) => res.body ?? {}));

  }

  scanWithDialog(doorAntenna: DoorAntenna, tagsObserver: (value: TagsList) => void): void {
    const modalRef = this.modalService.open(ScannerDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.loading = true;
    modalRef.componentInstance.doorAntenna = doorAntenna;
    this.scan(doorAntenna).subscribe(t => {
      modalRef.componentInstance.tagsList = t;
      modalRef.componentInstance.loading = false;
    });
    // modalRef.componentInstance.tagsList = this.tempTagsList();
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === ScannerDialogComponent.CONFIRM && modalRef.componentInstance.tagsList) {
        tagsObserver(modalRef.componentInstance.tagsList);
      }
    });
  }

}
