import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBay, getBayIdentifier } from '../bay.model';

export type EntityResponseType = HttpResponse<IBay>;
export type EntityArrayResponseType = HttpResponse<IBay[]>;

@Injectable({ providedIn: 'root' })
export class BayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bay: IBay): Observable<EntityResponseType> {
    return this.http.post<IBay>(this.resourceUrl, bay, { observe: 'response' });
  }

  update(bay: IBay): Observable<EntityResponseType> {
    return this.http.put<IBay>(`${this.resourceUrl}/${getBayIdentifier(bay) as number}`, bay, { observe: 'response' });
  }

  partialUpdate(bay: IBay): Observable<EntityResponseType> {
    return this.http.patch<IBay>(`${this.resourceUrl}/${getBayIdentifier(bay) as number}`, bay, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBay>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBay[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBayToCollectionIfMissing(bayCollection: IBay[], ...baysToCheck: (IBay | null | undefined)[]): IBay[] {
    const bays: IBay[] = baysToCheck.filter(isPresent);
    if (bays.length > 0) {
      const bayCollectionIdentifiers = bayCollection.map(bayItem => getBayIdentifier(bayItem)!);
      const baysToAdd = bays.filter(bayItem => {
        const bayIdentifier = getBayIdentifier(bayItem);
        if (bayIdentifier == null || bayCollectionIdentifiers.includes(bayIdentifier)) {
          return false;
        }
        bayCollectionIdentifiers.push(bayIdentifier);
        return true;
      });
      return [...baysToAdd, ...bayCollection];
    }
    return bayCollection;
  }
}
