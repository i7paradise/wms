import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUHFRFIDAntenna, getUHFRFIDAntennaIdentifier } from '../uhfrfid-antenna.model';

export type EntityResponseType = HttpResponse<IUHFRFIDAntenna>;
export type EntityArrayResponseType = HttpResponse<IUHFRFIDAntenna[]>;

@Injectable({ providedIn: 'root' })
export class UHFRFIDAntennaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/uhfrfid-antennas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(uHFRFIDAntenna: IUHFRFIDAntenna): Observable<EntityResponseType> {
    return this.http.post<IUHFRFIDAntenna>(this.resourceUrl, uHFRFIDAntenna, { observe: 'response' });
  }

  update(uHFRFIDAntenna: IUHFRFIDAntenna): Observable<EntityResponseType> {
    return this.http.put<IUHFRFIDAntenna>(`${this.resourceUrl}/${getUHFRFIDAntennaIdentifier(uHFRFIDAntenna) as number}`, uHFRFIDAntenna, {
      observe: 'response',
    });
  }

  partialUpdate(uHFRFIDAntenna: IUHFRFIDAntenna): Observable<EntityResponseType> {
    return this.http.patch<IUHFRFIDAntenna>(
      `${this.resourceUrl}/${getUHFRFIDAntennaIdentifier(uHFRFIDAntenna) as number}`,
      uHFRFIDAntenna,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUHFRFIDAntenna>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUHFRFIDAntenna[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUHFRFIDAntennaToCollectionIfMissing(
    uHFRFIDAntennaCollection: IUHFRFIDAntenna[],
    ...uHFRFIDAntennasToCheck: (IUHFRFIDAntenna | null | undefined)[]
  ): IUHFRFIDAntenna[] {
    const uHFRFIDAntennas: IUHFRFIDAntenna[] = uHFRFIDAntennasToCheck.filter(isPresent);
    if (uHFRFIDAntennas.length > 0) {
      const uHFRFIDAntennaCollectionIdentifiers = uHFRFIDAntennaCollection.map(
        uHFRFIDAntennaItem => getUHFRFIDAntennaIdentifier(uHFRFIDAntennaItem)!
      );
      const uHFRFIDAntennasToAdd = uHFRFIDAntennas.filter(uHFRFIDAntennaItem => {
        const uHFRFIDAntennaIdentifier = getUHFRFIDAntennaIdentifier(uHFRFIDAntennaItem);
        if (uHFRFIDAntennaIdentifier == null || uHFRFIDAntennaCollectionIdentifiers.includes(uHFRFIDAntennaIdentifier)) {
          return false;
        }
        uHFRFIDAntennaCollectionIdentifiers.push(uHFRFIDAntennaIdentifier);
        return true;
      });
      return [...uHFRFIDAntennasToAdd, ...uHFRFIDAntennaCollection];
    }
    return uHFRFIDAntennaCollection;
  }
}
