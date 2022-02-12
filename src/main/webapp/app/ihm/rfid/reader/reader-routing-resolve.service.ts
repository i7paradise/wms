import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUHFRFIDReader, UHFRFIDReader } from 'app/entities/uhfrfid-reader/uhfrfid-reader.model';
import { ReaderService } from './reader.service';

@Injectable({
  providedIn: 'root'
})
export class ReaderRoutingResolveService implements Resolve<IUHFRFIDReader> {
  constructor(protected service: ReaderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUHFRFIDReader> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((uHFRFIDReader: HttpResponse<UHFRFIDReader>) => {
          if (uHFRFIDReader.body) {
            return of(uHFRFIDReader.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UHFRFIDReader());
  }
}
