import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderContainer, OrderContainer } from '../order-container.model';
import { OrderContainerService } from '../service/order-container.service';

@Injectable({ providedIn: 'root' })
export class OrderContainerRoutingResolveService implements Resolve<IOrderContainer> {
  constructor(protected service: OrderContainerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderContainer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orderContainer: HttpResponse<OrderContainer>) => {
          if (orderContainer.body) {
            return of(orderContainer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrderContainer());
  }
}
