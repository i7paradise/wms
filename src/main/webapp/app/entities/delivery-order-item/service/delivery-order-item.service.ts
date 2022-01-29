import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliveryOrderItem, getDeliveryOrderItemIdentifier } from '../delivery-order-item.model';

export type EntityResponseType = HttpResponse<IDeliveryOrderItem>;
export type EntityArrayResponseType = HttpResponse<IDeliveryOrderItem[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryOrderItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery-order-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deliveryOrderItem: IDeliveryOrderItem): Observable<EntityResponseType> {
    return this.http.post<IDeliveryOrderItem>(this.resourceUrl, deliveryOrderItem, { observe: 'response' });
  }

  update(deliveryOrderItem: IDeliveryOrderItem): Observable<EntityResponseType> {
    return this.http.put<IDeliveryOrderItem>(
      `${this.resourceUrl}/${getDeliveryOrderItemIdentifier(deliveryOrderItem) as number}`,
      deliveryOrderItem,
      { observe: 'response' }
    );
  }

  partialUpdate(deliveryOrderItem: IDeliveryOrderItem): Observable<EntityResponseType> {
    return this.http.patch<IDeliveryOrderItem>(
      `${this.resourceUrl}/${getDeliveryOrderItemIdentifier(deliveryOrderItem) as number}`,
      deliveryOrderItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeliveryOrderItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeliveryOrderItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeliveryOrderItemToCollectionIfMissing(
    deliveryOrderItemCollection: IDeliveryOrderItem[],
    ...deliveryOrderItemsToCheck: (IDeliveryOrderItem | null | undefined)[]
  ): IDeliveryOrderItem[] {
    const deliveryOrderItems: IDeliveryOrderItem[] = deliveryOrderItemsToCheck.filter(isPresent);
    if (deliveryOrderItems.length > 0) {
      const deliveryOrderItemCollectionIdentifiers = deliveryOrderItemCollection.map(
        deliveryOrderItemItem => getDeliveryOrderItemIdentifier(deliveryOrderItemItem)!
      );
      const deliveryOrderItemsToAdd = deliveryOrderItems.filter(deliveryOrderItemItem => {
        const deliveryOrderItemIdentifier = getDeliveryOrderItemIdentifier(deliveryOrderItemItem);
        if (deliveryOrderItemIdentifier == null || deliveryOrderItemCollectionIdentifiers.includes(deliveryOrderItemIdentifier)) {
          return false;
        }
        deliveryOrderItemCollectionIdentifiers.push(deliveryOrderItemIdentifier);
        return true;
      });
      return [...deliveryOrderItemsToAdd, ...deliveryOrderItemCollection];
    }
    return deliveryOrderItemCollection;
  }
}
