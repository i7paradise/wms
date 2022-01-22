import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDoorAntenna, getDoorAntennaIdentifier } from '../door-antenna.model';

export type EntityResponseType = HttpResponse<IDoorAntenna>;
export type EntityArrayResponseType = HttpResponse<IDoorAntenna[]>;

@Injectable({ providedIn: 'root' })
export class DoorAntennaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/door-antennas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(doorAntenna: IDoorAntenna): Observable<EntityResponseType> {
    return this.http.post<IDoorAntenna>(this.resourceUrl, doorAntenna, { observe: 'response' });
  }

  update(doorAntenna: IDoorAntenna): Observable<EntityResponseType> {
    return this.http.put<IDoorAntenna>(`${this.resourceUrl}/${getDoorAntennaIdentifier(doorAntenna) as number}`, doorAntenna, {
      observe: 'response',
    });
  }

  partialUpdate(doorAntenna: IDoorAntenna): Observable<EntityResponseType> {
    return this.http.patch<IDoorAntenna>(`${this.resourceUrl}/${getDoorAntennaIdentifier(doorAntenna) as number}`, doorAntenna, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDoorAntenna>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDoorAntenna[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDoorAntennaToCollectionIfMissing(
    doorAntennaCollection: IDoorAntenna[],
    ...doorAntennasToCheck: (IDoorAntenna | null | undefined)[]
  ): IDoorAntenna[] {
    const doorAntennas: IDoorAntenna[] = doorAntennasToCheck.filter(isPresent);
    if (doorAntennas.length > 0) {
      const doorAntennaCollectionIdentifiers = doorAntennaCollection.map(doorAntennaItem => getDoorAntennaIdentifier(doorAntennaItem)!);
      const doorAntennasToAdd = doorAntennas.filter(doorAntennaItem => {
        const doorAntennaIdentifier = getDoorAntennaIdentifier(doorAntennaItem);
        if (doorAntennaIdentifier == null || doorAntennaCollectionIdentifiers.includes(doorAntennaIdentifier)) {
          return false;
        }
        doorAntennaCollectionIdentifiers.push(doorAntennaIdentifier);
        return true;
      });
      return [...doorAntennasToAdd, ...doorAntennaCollection];
    }
    return doorAntennaCollection;
  }
}
