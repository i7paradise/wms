import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWHLevel, getWHLevelIdentifier } from '../wh-level.model';

export type EntityResponseType = HttpResponse<IWHLevel>;
export type EntityArrayResponseType = HttpResponse<IWHLevel[]>;

@Injectable({ providedIn: 'root' })
export class WHLevelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wh-levels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(wHLevel: IWHLevel): Observable<EntityResponseType> {
    return this.http.post<IWHLevel>(this.resourceUrl, wHLevel, { observe: 'response' });
  }

  update(wHLevel: IWHLevel): Observable<EntityResponseType> {
    return this.http.put<IWHLevel>(`${this.resourceUrl}/${getWHLevelIdentifier(wHLevel) as number}`, wHLevel, { observe: 'response' });
  }

  partialUpdate(wHLevel: IWHLevel): Observable<EntityResponseType> {
    return this.http.patch<IWHLevel>(`${this.resourceUrl}/${getWHLevelIdentifier(wHLevel) as number}`, wHLevel, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWHLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWHLevel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWHLevelToCollectionIfMissing(wHLevelCollection: IWHLevel[], ...wHLevelsToCheck: (IWHLevel | null | undefined)[]): IWHLevel[] {
    const wHLevels: IWHLevel[] = wHLevelsToCheck.filter(isPresent);
    if (wHLevels.length > 0) {
      const wHLevelCollectionIdentifiers = wHLevelCollection.map(wHLevelItem => getWHLevelIdentifier(wHLevelItem)!);
      const wHLevelsToAdd = wHLevels.filter(wHLevelItem => {
        const wHLevelIdentifier = getWHLevelIdentifier(wHLevelItem);
        if (wHLevelIdentifier == null || wHLevelCollectionIdentifiers.includes(wHLevelIdentifier)) {
          return false;
        }
        wHLevelCollectionIdentifiers.push(wHLevelIdentifier);
        return true;
      });
      return [...wHLevelsToAdd, ...wHLevelCollection];
    }
    return wHLevelCollection;
  }
}
