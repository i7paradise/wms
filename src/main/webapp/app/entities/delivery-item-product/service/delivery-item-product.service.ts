import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliveryItemProduct, getDeliveryItemProductIdentifier } from '../delivery-item-product.model';

export type EntityResponseType = HttpResponse<IDeliveryItemProduct>;
export type EntityArrayResponseType = HttpResponse<IDeliveryItemProduct[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryItemProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery-item-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deliveryItemProduct: IDeliveryItemProduct): Observable<EntityResponseType> {
    return this.http.post<IDeliveryItemProduct>(this.resourceUrl, deliveryItemProduct, { observe: 'response' });
  }

  update(deliveryItemProduct: IDeliveryItemProduct): Observable<EntityResponseType> {
    return this.http.put<IDeliveryItemProduct>(
      `${this.resourceUrl}/${getDeliveryItemProductIdentifier(deliveryItemProduct) as number}`,
      deliveryItemProduct,
      { observe: 'response' }
    );
  }

  partialUpdate(deliveryItemProduct: IDeliveryItemProduct): Observable<EntityResponseType> {
    return this.http.patch<IDeliveryItemProduct>(
      `${this.resourceUrl}/${getDeliveryItemProductIdentifier(deliveryItemProduct) as number}`,
      deliveryItemProduct,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeliveryItemProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeliveryItemProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeliveryItemProductToCollectionIfMissing(
    deliveryItemProductCollection: IDeliveryItemProduct[],
    ...deliveryItemProductsToCheck: (IDeliveryItemProduct | null | undefined)[]
  ): IDeliveryItemProduct[] {
    const deliveryItemProducts: IDeliveryItemProduct[] = deliveryItemProductsToCheck.filter(isPresent);
    if (deliveryItemProducts.length > 0) {
      const deliveryItemProductCollectionIdentifiers = deliveryItemProductCollection.map(
        deliveryItemProductItem => getDeliveryItemProductIdentifier(deliveryItemProductItem)!
      );
      const deliveryItemProductsToAdd = deliveryItemProducts.filter(deliveryItemProductItem => {
        const deliveryItemProductIdentifier = getDeliveryItemProductIdentifier(deliveryItemProductItem);
        if (deliveryItemProductIdentifier == null || deliveryItemProductCollectionIdentifiers.includes(deliveryItemProductIdentifier)) {
          return false;
        }
        deliveryItemProductCollectionIdentifiers.push(deliveryItemProductIdentifier);
        return true;
      });
      return [...deliveryItemProductsToAdd, ...deliveryItemProductCollection];
    }
    return deliveryItemProductCollection;
  }
}
