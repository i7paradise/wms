import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDoor, Door } from '../door.model';
import { DoorService } from '../service/door.service';

@Injectable({ providedIn: 'root' })
export class DoorRoutingResolveService implements Resolve<IDoor> {
  constructor(protected service: DoorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDoor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((door: HttpResponse<Door>) => {
          if (door.body) {
            return of(door.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Door());
  }
}
