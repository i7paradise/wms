import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUHFRFIDReader, getUHFRFIDReaderIdentifier } from '../uhfrfid-reader.model';

export type EntityResponseType = HttpResponse<IUHFRFIDReader>;
export type EntityArrayResponseType = HttpResponse<IUHFRFIDReader[]>;

@Injectable({ providedIn: 'root' })
export class UHFRFIDReaderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/uhfrfid-readers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(uHFRFIDReader: IUHFRFIDReader): Observable<EntityResponseType> {
    return this.http.post<IUHFRFIDReader>(this.resourceUrl, uHFRFIDReader, { observe: 'response' });
  }

  update(uHFRFIDReader: IUHFRFIDReader): Observable<EntityResponseType> {
    return this.http.put<IUHFRFIDReader>(`${this.resourceUrl}/${getUHFRFIDReaderIdentifier(uHFRFIDReader) as number}`, uHFRFIDReader, {
      observe: 'response',
    });
  }

  partialUpdate(uHFRFIDReader: IUHFRFIDReader): Observable<EntityResponseType> {
    return this.http.patch<IUHFRFIDReader>(`${this.resourceUrl}/${getUHFRFIDReaderIdentifier(uHFRFIDReader) as number}`, uHFRFIDReader, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUHFRFIDReader>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUHFRFIDReader[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUHFRFIDReaderToCollectionIfMissing(
    uHFRFIDReaderCollection: IUHFRFIDReader[],
    ...uHFRFIDReadersToCheck: (IUHFRFIDReader | null | undefined)[]
  ): IUHFRFIDReader[] {
    const uHFRFIDReaders: IUHFRFIDReader[] = uHFRFIDReadersToCheck.filter(isPresent);
    if (uHFRFIDReaders.length > 0) {
      const uHFRFIDReaderCollectionIdentifiers = uHFRFIDReaderCollection.map(
        uHFRFIDReaderItem => getUHFRFIDReaderIdentifier(uHFRFIDReaderItem)!
      );
      const uHFRFIDReadersToAdd = uHFRFIDReaders.filter(uHFRFIDReaderItem => {
        const uHFRFIDReaderIdentifier = getUHFRFIDReaderIdentifier(uHFRFIDReaderItem);
        if (uHFRFIDReaderIdentifier == null || uHFRFIDReaderCollectionIdentifiers.includes(uHFRFIDReaderIdentifier)) {
          return false;
        }
        uHFRFIDReaderCollectionIdentifiers.push(uHFRFIDReaderIdentifier);
        return true;
      });
      return [...uHFRFIDReadersToAdd, ...uHFRFIDReaderCollection];
    }
    return uHFRFIDReaderCollection;
  }
}
