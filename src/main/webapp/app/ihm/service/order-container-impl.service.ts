import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IOrderContainer } from 'app/entities/order-container/order-container.model';
import { OrderContainerService } from 'app/entities/order-container/service/order-container.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { map, Observable } from 'rxjs';
import { TagsList } from 'app/ihm/model/tags-list.model';
import { IOrderItemProduct } from 'app/entities/order-item-product/order-item-product.model';

@Injectable({
  providedIn: 'root',
})
export class OrderContainerImplService extends OrderContainerService {
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {
    super(http, applicationConfigService);
    this.resourceUrl = this.applicationConfigService.getEndpointFor('api/v1/order-containers');
  }

  findOrderContainers(orderItem: IOrderItem): Observable<IOrderContainer[]> {
    return this.http
      .get<IOrderContainer[]>(
        `${this.resourceUrl}/from-order-item/${orderItem.id as number}`,
        { observe: 'response' }
      )
      .pipe(map(res => res.body ?? []));
  }

  createOrderContainersWithTags(orderItem: IOrderItem, tagsList: TagsList): void {
    if (!tagsList.tags || tagsList.tags.length === 0) {
      return;
    }

    this.http
      .post<IOrderContainer[]>(
        `${this.resourceUrl}/create`,
        {
          orderItemId: orderItem.id,
          tagsList,
        },
        { observe: 'response' }
      )
      .pipe(map(res => res.body ?? []))
      .subscribe(list => (orderItem.orderContainers = list));
  }

  createOrderItemProducts(container: IOrderContainer, tagsList: TagsList): Observable<IOrderItemProduct[]> {
    if (!tagsList.tags || tagsList.tags.length === 0) {
      throw '';
    }

    return this.http
      .post<IOrderItemProduct[]>(
        `${this.resourceUrl}/${container.id!}/create-item-products`,
        tagsList,
        { observe: 'response' }
      )
      .pipe(map(res => res.body ?? []));
  }

  deletePackages(containers: IOrderContainer[]): Observable<HttpResponse<{}>> {
    const request = {
      ids : containers.map(e => e.id!)
    };
    return this.http.post(`${this.resourceUrl}/delete-item-products`, request, { observe: 'response' });
  }

  deleteList(containers: IOrderContainer[]): Observable<HttpResponse<{}>> {
    const request = {
      ids : containers.map(e => e.id!)
    };
    return this.http.post(`${this.resourceUrl}/delete`, request, { observe: 'response' });
  }
}
