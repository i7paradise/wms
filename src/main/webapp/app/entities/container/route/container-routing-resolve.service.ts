import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContainer, Container } from '../container.model';
import { ContainerService } from '../service/container.service';

@Injectable({ providedIn: 'root' })
export class ContainerRoutingResolveService implements Resolve<IContainer> {
  constructor(protected service: ContainerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContainer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((container: HttpResponse<Container>) => {
          if (container.body) {
            return of(container.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Container());
  }
}
