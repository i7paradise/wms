import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { EntityResponseType } from 'app/entities/warehouse/service/warehouse.service';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class XspsWarehouseService {

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v1/reader');

  constructor(protected http: HttpClient, 
    protected applicationConfigService: ApplicationConfigService) { }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWarehouse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
