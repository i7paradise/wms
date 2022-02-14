import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { ScannerDialogComponent } from './scanner-dialog/scanner-dialog.component';
import { TagsList } from './tags-list.model';

@Injectable({
  providedIn: 'root',
})
export class ScannerService {
  constructor(protected modalService: NgbModal) {}

  scan(): Observable<TagsList> {

    return new Observable(subscriber => {
      setTimeout(() => {
        subscriber.next(this.tempTagsList());
      }, 1500);
    });

  }

  scanWithDialog(tagsObserver: (value: TagsList) => void): void {
    const modalRef = this.modalService.open(ScannerDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.loading = true;
    this.scan().subscribe(t => {
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

  private tempTagsList(): TagsList {
    const tags = [];
    for (let i = 0; i < 10; i++) {
      tags.push(this.randomString());
    }
    return new TagsList(tags);
  }

  private randomString(): string {
    return Math.random()
      .toString(36)
      .replace(/[^a-z]+/g, '')
      .substr(0, 5);
  }
}
