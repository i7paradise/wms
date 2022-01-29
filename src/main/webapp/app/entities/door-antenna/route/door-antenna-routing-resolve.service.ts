import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDoorAntenna, DoorAntenna } from '../door-antenna.model';
import { DoorAntennaService } from '../service/door-antenna.service';

@Injectable({ providedIn: 'root' })
export class DoorAntennaRoutingResolveService implements Resolve<IDoorAntenna> {
  constructor(protected service: DoorAntennaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDoorAntenna> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((doorAntenna: HttpResponse<DoorAntenna>) => {
          if (doorAntenna.body) {
            return of(doorAntenna.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DoorAntenna());
  }
}
