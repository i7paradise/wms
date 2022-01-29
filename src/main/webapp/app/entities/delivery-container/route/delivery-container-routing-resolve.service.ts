import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliveryContainer, DeliveryContainer } from '../delivery-container.model';
import { DeliveryContainerService } from '../service/delivery-container.service';

@Injectable({ providedIn: 'root' })
export class DeliveryContainerRoutingResolveService implements Resolve<IDeliveryContainer> {
  constructor(protected service: DeliveryContainerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeliveryContainer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deliveryContainer: HttpResponse<DeliveryContainer>) => {
          if (deliveryContainer.body) {
            return of(deliveryContainer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeliveryContainer());
  }
}
