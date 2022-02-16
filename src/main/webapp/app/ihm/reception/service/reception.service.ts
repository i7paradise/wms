import { Injectable } from '@angular/core';
import { IOrder } from 'app/entities/order/order.model';
import { IOrderContainer } from 'app/entities/order-container/order-container.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { createRequestOption } from 'app/core/request/request-util';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemProduct } from 'app/entities/order-item-product/order-item-product.model';
import { TagsList } from 'app/ihm/model/tags-list.model';

export type EntityResponseType = HttpResponse<IOrder>;
export type EntityArrayResponseType = HttpResponse<IOrder[]>;

@Injectable({
  providedIn: 'root',
})
export class ReceptionService extends OrderService {

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {
    super(http, applicationConfigService);
    this.resourceUrl = this.applicationConfigService.getEndpointFor('api/v1/receptions');
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  createOrderItemProducts(container: IOrderContainer, tagsList: TagsList): void {
    if (!tagsList.tags || tagsList.tags.length === 0) {
      return;
    }

    if (!container.orderItemProducts) {
      container.orderItemProducts = [];
    }

    const orderItemProducts = tagsList.tags.map(e => ({
      ...new OrderItemProduct(),
      rfidTAG: e,
      orderContainer: { id: container.id },
    }));
    // TODO send orderItemProducts to api/v1/

    orderItemProducts.forEach(e => container.orderItemProducts?.push(e));
  }

}
