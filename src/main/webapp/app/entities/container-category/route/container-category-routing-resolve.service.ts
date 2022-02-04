import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContainerCategory, ContainerCategory } from '../container-category.model';
import { ContainerCategoryService } from '../service/container-category.service';

@Injectable({ providedIn: 'root' })
export class ContainerCategoryRoutingResolveService implements Resolve<IContainerCategory> {
  constructor(protected service: ContainerCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContainerCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((containerCategory: HttpResponse<ContainerCategory>) => {
          if (containerCategory.body) {
            return of(containerCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContainerCategory());
  }
}
