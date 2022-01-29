import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWHRow, getWHRowIdentifier } from '../wh-row.model';

export type EntityResponseType = HttpResponse<IWHRow>;
export type EntityArrayResponseType = HttpResponse<IWHRow[]>;

@Injectable({ providedIn: 'root' })
export class WHRowService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wh-rows');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(wHRow: IWHRow): Observable<EntityResponseType> {
    return this.http.post<IWHRow>(this.resourceUrl, wHRow, { observe: 'response' });
  }

  update(wHRow: IWHRow): Observable<EntityResponseType> {
    return this.http.put<IWHRow>(`${this.resourceUrl}/${getWHRowIdentifier(wHRow) as number}`, wHRow, { observe: 'response' });
  }

  partialUpdate(wHRow: IWHRow): Observable<EntityResponseType> {
    return this.http.patch<IWHRow>(`${this.resourceUrl}/${getWHRowIdentifier(wHRow) as number}`, wHRow, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWHRow>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWHRow[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWHRowToCollectionIfMissing(wHRowCollection: IWHRow[], ...wHRowsToCheck: (IWHRow | null | undefined)[]): IWHRow[] {
    const wHRows: IWHRow[] = wHRowsToCheck.filter(isPresent);
    if (wHRows.length > 0) {
      const wHRowCollectionIdentifiers = wHRowCollection.map(wHRowItem => getWHRowIdentifier(wHRowItem)!);
      const wHRowsToAdd = wHRows.filter(wHRowItem => {
        const wHRowIdentifier = getWHRowIdentifier(wHRowItem);
        if (wHRowIdentifier == null || wHRowCollectionIdentifiers.includes(wHRowIdentifier)) {
          return false;
        }
        wHRowCollectionIdentifiers.push(wHRowIdentifier);
        return true;
      });
      return [...wHRowsToAdd, ...wHRowCollection];
    }
    return wHRowCollection;
  }
}
