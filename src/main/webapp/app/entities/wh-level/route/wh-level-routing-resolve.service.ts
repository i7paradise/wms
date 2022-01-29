import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWHLevel, WHLevel } from '../wh-level.model';
import { WHLevelService } from '../service/wh-level.service';

@Injectable({ providedIn: 'root' })
export class WHLevelRoutingResolveService implements Resolve<IWHLevel> {
  constructor(protected service: WHLevelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWHLevel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wHLevel: HttpResponse<WHLevel>) => {
          if (wHLevel.body) {
            return of(wHLevel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WHLevel());
  }
}
