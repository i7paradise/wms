import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWarehouse, getWarehouseIdentifier } from '../warehouse.model';

export type EntityResponseType = HttpResponse<IWarehouse>;
export type EntityArrayResponseType = HttpResponse<IWarehouse[]>;

@Injectable({ providedIn: 'root' })
export class WarehouseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/warehouses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(warehouse: IWarehouse): Observable<EntityResponseType> {
    return this.http.post<IWarehouse>(this.resourceUrl, warehouse, { observe: 'response' });
  }

  update(warehouse: IWarehouse): Observable<EntityResponseType> {
    return this.http.put<IWarehouse>(`${this.resourceUrl}/${getWarehouseIdentifier(warehouse) as number}`, warehouse, {
      observe: 'response',
    });
  }

  partialUpdate(warehouse: IWarehouse): Observable<EntityResponseType> {
    return this.http.patch<IWarehouse>(`${this.resourceUrl}/${getWarehouseIdentifier(warehouse) as number}`, warehouse, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWarehouse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWarehouse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWarehouseToCollectionIfMissing(
    warehouseCollection: IWarehouse[],
    ...warehousesToCheck: (IWarehouse | null | undefined)[]
  ): IWarehouse[] {
    const warehouses: IWarehouse[] = warehousesToCheck.filter(isPresent);
    if (warehouses.length > 0) {
      const warehouseCollectionIdentifiers = warehouseCollection.map(warehouseItem => getWarehouseIdentifier(warehouseItem)!);
      const warehousesToAdd = warehouses.filter(warehouseItem => {
        const warehouseIdentifier = getWarehouseIdentifier(warehouseItem);
        if (warehouseIdentifier == null || warehouseCollectionIdentifiers.includes(warehouseIdentifier)) {
          return false;
        }
        warehouseCollectionIdentifiers.push(warehouseIdentifier);
        return true;
      });
      return [...warehousesToAdd, ...warehouseCollection];
    }
    return warehouseCollection;
  }
}
