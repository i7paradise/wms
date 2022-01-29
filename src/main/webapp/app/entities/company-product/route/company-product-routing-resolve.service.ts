import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompanyProduct, CompanyProduct } from '../company-product.model';
import { CompanyProductService } from '../service/company-product.service';

@Injectable({ providedIn: 'root' })
export class CompanyProductRoutingResolveService implements Resolve<ICompanyProduct> {
  constructor(protected service: CompanyProductService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompanyProduct> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((companyProduct: HttpResponse<CompanyProduct>) => {
          if (companyProduct.body) {
            return of(companyProduct.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CompanyProduct());
  }
}
