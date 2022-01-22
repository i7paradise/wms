import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWHRow, WHRow } from '../wh-row.model';
import { WHRowService } from '../service/wh-row.service';

@Injectable({ providedIn: 'root' })
export class WHRowRoutingResolveService implements Resolve<IWHRow> {
  constructor(protected service: WHRowService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWHRow> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wHRow: HttpResponse<WHRow>) => {
          if (wHRow.body) {
            return of(wHRow.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WHRow());
  }
}
