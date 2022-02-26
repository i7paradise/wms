import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { map, Observable } from 'rxjs';
import { ScannerDialogComponent } from './scanner-dialog/scanner-dialog.component';
import { TagsList } from '../model/tags-list.model';
import { ScanRequest } from '../model/scan-request.model';

@Injectable({
  providedIn: 'root',
})
export class ScannerService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/v1/tags');

  constructor(private applicationConfigService: ApplicationConfigService,
    protected http: HttpClient,
    protected modalService: NgbModal) {}

  scan(rfidAntenna: IUHFRFIDAntenna): Observable<TagsList> {
    if (!rfidAntenna.id) {
      throw 'can not call scanner service with null IUHFRFIDAntenna.id';
    }
    const request = new ScanRequest(rfidAntenna.id);
    return this.http
      .post<TagsList>(this.resourceUrl + '/scan', request, { observe: 'response' })
      .pipe(map((res) => res.body ?? {}));

  }

  scanWithDialog(rfidAntenna: IUHFRFIDAntenna, tagsObserver: (value: TagsList) => void): void {
    const modalRef = this.modalService.open(ScannerDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.loading = true;
    modalRef.componentInstance.rfidAntenna = rfidAntenna;
    this.scan(rfidAntenna).subscribe(t => {
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
