import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderItemProduct, getOrderItemProductIdentifier } from '../order-item-product.model';

export type EntityResponseType = HttpResponse<IOrderItemProduct>;
export type EntityArrayResponseType = HttpResponse<IOrderItemProduct[]>;

@Injectable({ providedIn: 'root' })
export class OrderItemProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-item-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderItemProduct: IOrderItemProduct): Observable<EntityResponseType> {
    return this.http.post<IOrderItemProduct>(this.resourceUrl, orderItemProduct, { observe: 'response' });
  }

  update(orderItemProduct: IOrderItemProduct): Observable<EntityResponseType> {
    return this.http.put<IOrderItemProduct>(
      `${this.resourceUrl}/${getOrderItemProductIdentifier(orderItemProduct) as number}`,
      orderItemProduct,
      { observe: 'response' }
    );
  }

  partialUpdate(orderItemProduct: IOrderItemProduct): Observable<EntityResponseType> {
    return this.http.patch<IOrderItemProduct>(
      `${this.resourceUrl}/${getOrderItemProductIdentifier(orderItemProduct) as number}`,
      orderItemProduct,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrderItemProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrderItemProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrderItemProductToCollectionIfMissing(
    orderItemProductCollection: IOrderItemProduct[],
    ...orderItemProductsToCheck: (IOrderItemProduct | null | undefined)[]
  ): IOrderItemProduct[] {
    const orderItemProducts: IOrderItemProduct[] = orderItemProductsToCheck.filter(isPresent);
    if (orderItemProducts.length > 0) {
      const orderItemProductCollectionIdentifiers = orderItemProductCollection.map(
        orderItemProductItem => getOrderItemProductIdentifier(orderItemProductItem)!
      );
      const orderItemProductsToAdd = orderItemProducts.filter(orderItemProductItem => {
        const orderItemProductIdentifier = getOrderItemProductIdentifier(orderItemProductItem);
        if (orderItemProductIdentifier == null || orderItemProductCollectionIdentifiers.includes(orderItemProductIdentifier)) {
          return false;
        }
        orderItemProductCollectionIdentifiers.push(orderItemProductIdentifier);
        return true;
      });
      return [...orderItemProductsToAdd, ...orderItemProductCollection];
    }
    return orderItemProductCollection;
  }
}
