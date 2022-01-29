import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWarehouse, Warehouse } from '../warehouse.model';
import { WarehouseService } from '../service/warehouse.service';

@Injectable({ providedIn: 'root' })
export class WarehouseRoutingResolveService implements Resolve<IWarehouse> {
  constructor(protected service: WarehouseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWarehouse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((warehouse: HttpResponse<Warehouse>) => {
          if (warehouse.body) {
            return of(warehouse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Warehouse());
  }
}
