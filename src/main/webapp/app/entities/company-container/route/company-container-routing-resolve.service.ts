import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompanyContainer, CompanyContainer } from '../company-container.model';
import { CompanyContainerService } from '../service/company-container.service';

@Injectable({ providedIn: 'root' })
export class CompanyContainerRoutingResolveService implements Resolve<ICompanyContainer> {
  constructor(protected service: CompanyContainerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompanyContainer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((companyContainer: HttpResponse<CompanyContainer>) => {
          if (companyContainer.body) {
            return of(companyContainer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CompanyContainer());
  }
}
