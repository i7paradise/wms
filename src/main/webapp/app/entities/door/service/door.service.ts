import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDoor, getDoorIdentifier } from '../door.model';

export type EntityResponseType = HttpResponse<IDoor>;
export type EntityArrayResponseType = HttpResponse<IDoor[]>;

@Injectable({ providedIn: 'root' })
export class DoorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/doors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(door: IDoor): Observable<EntityResponseType> {
    return this.http.post<IDoor>(this.resourceUrl, door, { observe: 'response' });
  }

  update(door: IDoor): Observable<EntityResponseType> {
    return this.http.put<IDoor>(`${this.resourceUrl}/${getDoorIdentifier(door) as number}`, door, { observe: 'response' });
  }

  partialUpdate(door: IDoor): Observable<EntityResponseType> {
    return this.http.patch<IDoor>(`${this.resourceUrl}/${getDoorIdentifier(door) as number}`, door, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDoor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDoor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDoorToCollectionIfMissing(doorCollection: IDoor[], ...doorsToCheck: (IDoor | null | undefined)[]): IDoor[] {
    const doors: IDoor[] = doorsToCheck.filter(isPresent);
    if (doors.length > 0) {
      const doorCollectionIdentifiers = doorCollection.map(doorItem => getDoorIdentifier(doorItem)!);
      const doorsToAdd = doors.filter(doorItem => {
        const doorIdentifier = getDoorIdentifier(doorItem);
        if (doorIdentifier == null || doorCollectionIdentifiers.includes(doorIdentifier)) {
          return false;
        }
        doorCollectionIdentifiers.push(doorIdentifier);
        return true;
      });
      return [...doorsToAdd, ...doorCollection];
    }
    return doorCollection;
  }
}
