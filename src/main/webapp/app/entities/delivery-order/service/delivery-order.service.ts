import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliveryOrder, getDeliveryOrderIdentifier } from '../delivery-order.model';

export type EntityResponseType = HttpResponse<IDeliveryOrder>;
export type EntityArrayResponseType = HttpResponse<IDeliveryOrder[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deliveryOrder: IDeliveryOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliveryOrder);
    return this.http
      .post<IDeliveryOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(deliveryOrder: IDeliveryOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliveryOrder);
    return this.http
      .put<IDeliveryOrder>(`${this.resourceUrl}/${getDeliveryOrderIdentifier(deliveryOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(deliveryOrder: IDeliveryOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliveryOrder);
    return this.http
      .patch<IDeliveryOrder>(`${this.resourceUrl}/${getDeliveryOrderIdentifier(deliveryOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDeliveryOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeliveryOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeliveryOrderToCollectionIfMissing(
    deliveryOrderCollection: IDeliveryOrder[],
    ...deliveryOrdersToCheck: (IDeliveryOrder | null | undefined)[]
  ): IDeliveryOrder[] {
    const deliveryOrders: IDeliveryOrder[] = deliveryOrdersToCheck.filter(isPresent);
    if (deliveryOrders.length > 0) {
      const deliveryOrderCollectionIdentifiers = deliveryOrderCollection.map(
        deliveryOrderItem => getDeliveryOrderIdentifier(deliveryOrderItem)!
      );
      const deliveryOrdersToAdd = deliveryOrders.filter(deliveryOrderItem => {
        const deliveryOrderIdentifier = getDeliveryOrderIdentifier(deliveryOrderItem);
        if (deliveryOrderIdentifier == null || deliveryOrderCollectionIdentifiers.includes(deliveryOrderIdentifier)) {
          return false;
        }
        deliveryOrderCollectionIdentifiers.push(deliveryOrderIdentifier);
        return true;
      });
      return [...deliveryOrdersToAdd, ...deliveryOrderCollection];
    }
    return deliveryOrderCollection;
  }

  protected convertDateFromClient(deliveryOrder: IDeliveryOrder): IDeliveryOrder {
    return Object.assign({}, deliveryOrder, {
      placedDate: deliveryOrder.placedDate?.isValid() ? deliveryOrder.placedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.placedDate = res.body.placedDate ? dayjs(res.body.placedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((deliveryOrder: IDeliveryOrder) => {
        deliveryOrder.placedDate = deliveryOrder.placedDate ? dayjs(deliveryOrder.placedDate) : undefined;
      });
    }
    return res;
  }
}
