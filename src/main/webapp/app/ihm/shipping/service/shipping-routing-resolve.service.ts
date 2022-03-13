import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { ShippingService } from './shipping.service';
import { Order, IOrder } from 'app/entities/order/order.model';

@Injectable({ providedIn: 'root' })
export class ShippingRoutingResolveService implements Resolve<IOrder> {
  constructor(protected service: ShippingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((order: HttpResponse<IOrder>) => {
          if (order.body) {
            return of(order.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Order());
  }
}
