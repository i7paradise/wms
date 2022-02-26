import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderContainer, getOrderContainerIdentifier } from '../order-container.model';

export type EntityResponseType = HttpResponse<IOrderContainer>;
export type EntityArrayResponseType = HttpResponse<IOrderContainer[]>;

@Injectable({ providedIn: 'root' })
export class OrderContainerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-containers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderContainer: IOrderContainer): Observable<EntityResponseType> {
    return this.http.post<IOrderContainer>(this.resourceUrl, orderContainer, { observe: 'response' });
  }

  update(orderContainer: IOrderContainer): Observable<EntityResponseType> {
    return this.http.put<IOrderContainer>(`${this.resourceUrl}/${getOrderContainerIdentifier(orderContainer) as number}`, orderContainer, {
      observe: 'response',
    });
  }

  partialUpdate(orderContainer: IOrderContainer): Observable<EntityResponseType> {
    return this.http.patch<IOrderContainer>(
      `${this.resourceUrl}/${getOrderContainerIdentifier(orderContainer) as number}`,
      orderContainer,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrderContainer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrderContainer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrderContainerToCollectionIfMissing(
    orderContainerCollection: IOrderContainer[],
    ...orderContainersToCheck: (IOrderContainer | null | undefined)[]
  ): IOrderContainer[] {
    const orderContainers: IOrderContainer[] = orderContainersToCheck.filter(isPresent);
    if (orderContainers.length > 0) {
      const orderContainerCollectionIdentifiers = orderContainerCollection.map(
        orderContainerItem => getOrderContainerIdentifier(orderContainerItem)!
      );
      const orderContainersToAdd = orderContainers.filter(orderContainerItem => {
        const orderContainerIdentifier = getOrderContainerIdentifier(orderContainerItem);
        if (orderContainerIdentifier == null || orderContainerCollectionIdentifiers.includes(orderContainerIdentifier)) {
          return false;
        }
        orderContainerCollectionIdentifiers.push(orderContainerIdentifier);
        return true;
      });
      return [...orderContainersToAdd, ...orderContainerCollection];
    }
    return orderContainerCollection;
  }
}
