import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliveryContainer, getDeliveryContainerIdentifier } from '../delivery-container.model';

export type EntityResponseType = HttpResponse<IDeliveryContainer>;
export type EntityArrayResponseType = HttpResponse<IDeliveryContainer[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryContainerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery-containers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deliveryContainer: IDeliveryContainer): Observable<EntityResponseType> {
    return this.http.post<IDeliveryContainer>(this.resourceUrl, deliveryContainer, { observe: 'response' });
  }

  update(deliveryContainer: IDeliveryContainer): Observable<EntityResponseType> {
    return this.http.put<IDeliveryContainer>(
      `${this.resourceUrl}/${getDeliveryContainerIdentifier(deliveryContainer) as number}`,
      deliveryContainer,
      { observe: 'response' }
    );
  }

  partialUpdate(deliveryContainer: IDeliveryContainer): Observable<EntityResponseType> {
    return this.http.patch<IDeliveryContainer>(
      `${this.resourceUrl}/${getDeliveryContainerIdentifier(deliveryContainer) as number}`,
      deliveryContainer,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeliveryContainer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeliveryContainer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeliveryContainerToCollectionIfMissing(
    deliveryContainerCollection: IDeliveryContainer[],
    ...deliveryContainersToCheck: (IDeliveryContainer | null | undefined)[]
  ): IDeliveryContainer[] {
    const deliveryContainers: IDeliveryContainer[] = deliveryContainersToCheck.filter(isPresent);
    if (deliveryContainers.length > 0) {
      const deliveryContainerCollectionIdentifiers = deliveryContainerCollection.map(
        deliveryContainerItem => getDeliveryContainerIdentifier(deliveryContainerItem)!
      );
      const deliveryContainersToAdd = deliveryContainers.filter(deliveryContainerItem => {
        const deliveryContainerIdentifier = getDeliveryContainerIdentifier(deliveryContainerItem);
        if (deliveryContainerIdentifier == null || deliveryContainerCollectionIdentifiers.includes(deliveryContainerIdentifier)) {
          return false;
        }
        deliveryContainerCollectionIdentifiers.push(deliveryContainerIdentifier);
        return true;
      });
      return [...deliveryContainersToAdd, ...deliveryContainerCollection];
    }
    return deliveryContainerCollection;
  }
}
