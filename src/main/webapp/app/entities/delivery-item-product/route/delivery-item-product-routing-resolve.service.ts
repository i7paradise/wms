import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliveryItemProduct, DeliveryItemProduct } from '../delivery-item-product.model';
import { DeliveryItemProductService } from '../service/delivery-item-product.service';

@Injectable({ providedIn: 'root' })
export class DeliveryItemProductRoutingResolveService implements Resolve<IDeliveryItemProduct> {
  constructor(protected service: DeliveryItemProductService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeliveryItemProduct> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deliveryItemProduct: HttpResponse<DeliveryItemProduct>) => {
          if (deliveryItemProduct.body) {
            return of(deliveryItemProduct.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeliveryItemProduct());
  }
}
