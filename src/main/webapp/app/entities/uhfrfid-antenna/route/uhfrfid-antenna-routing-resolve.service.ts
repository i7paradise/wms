import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUHFRFIDAntenna, UHFRFIDAntenna } from '../uhfrfid-antenna.model';
import { UHFRFIDAntennaService } from '../service/uhfrfid-antenna.service';

@Injectable({ providedIn: 'root' })
export class UHFRFIDAntennaRoutingResolveService implements Resolve<IUHFRFIDAntenna> {
  constructor(protected service: UHFRFIDAntennaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUHFRFIDAntenna> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((uHFRFIDAntenna: HttpResponse<UHFRFIDAntenna>) => {
          if (uHFRFIDAntenna.body) {
            return of(uHFRFIDAntenna.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UHFRFIDAntenna());
  }
}
