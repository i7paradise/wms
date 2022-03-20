import { IOrderItem, OrderItem } from 'app/entities/order-item/order-item.model';
import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { ShippingService } from './shipping.service';



@Injectable({
  providedIn: 'root'
})
export class ShippingItemRoutingResolveService implements Resolve<IOrderItem> {
  // TODO shippingService 
  constructor(protected service: ShippingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findOrderItem(id).pipe(
        mergeMap((orderItem: HttpResponse<OrderItem>) => {
          if (orderItem.body) {
            return of(orderItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrderItem());
  }
}