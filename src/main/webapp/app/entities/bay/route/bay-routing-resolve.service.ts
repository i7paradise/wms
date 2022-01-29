import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBay, Bay } from '../bay.model';
import { BayService } from '../service/bay.service';

@Injectable({ providedIn: 'root' })
export class BayRoutingResolveService implements Resolve<IBay> {
  constructor(protected service: BayService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBay> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bay: HttpResponse<Bay>) => {
          if (bay.body) {
            return of(bay.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bay());
  }
}
