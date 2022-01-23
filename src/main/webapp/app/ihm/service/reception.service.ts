import { Injectable } from '@angular/core';
import { IDeliveryOrder } from 'app/entities/delivery-order/delivery-order.model';
import { DeliveryOrderService } from 'app/entities/delivery-order/service/delivery-order.service';
import { createRequestOption } from 'app/core/request/request-util';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

export type EntityResponseType = HttpResponse<IDeliveryOrder>;
export type EntityArrayResponseType = HttpResponse<IDeliveryOrder[]>;

@Injectable({
  providedIn: 'root',
})
export class ReceptionService extends DeliveryOrderService {
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {
    super(http, applicationConfigService);
    this.resourceUrl = this.applicationConfigService.getEndpointFor('api/v1/receptions');
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeliveryOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
}
