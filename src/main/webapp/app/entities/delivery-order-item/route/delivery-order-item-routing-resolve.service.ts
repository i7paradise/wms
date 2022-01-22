import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliveryOrderItem, DeliveryOrderItem } from '../delivery-order-item.model';
import { DeliveryOrderItemService } from '../service/delivery-order-item.service';

@Injectable({ providedIn: 'root' })
export class DeliveryOrderItemRoutingResolveService implements Resolve<IDeliveryOrderItem> {
  constructor(protected service: DeliveryOrderItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeliveryOrderItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deliveryOrderItem: HttpResponse<DeliveryOrderItem>) => {
          if (deliveryOrderItem.body) {
            return of(deliveryOrderItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeliveryOrderItem());
  }
}
