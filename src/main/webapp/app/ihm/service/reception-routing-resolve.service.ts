import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { ReceptionService } from './reception.service';
import { DeliveryOrder, IDeliveryOrder } from 'app/entities/delivery-order/delivery-order.model';

@Injectable({ providedIn: 'root' })
export class ReceptionRoutingResolveService implements Resolve<IDeliveryOrder> {
  constructor(protected service: ReceptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeliveryOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deliveryOrder: HttpResponse<DeliveryOrder>) => {
          if (deliveryOrder.body) {
            return of(deliveryOrder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeliveryOrder());
  }
}
