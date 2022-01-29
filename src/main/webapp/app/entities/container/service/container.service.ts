import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContainer, getContainerIdentifier } from '../container.model';

export type EntityResponseType = HttpResponse<IContainer>;
export type EntityArrayResponseType = HttpResponse<IContainer[]>;

@Injectable({ providedIn: 'root' })
export class ContainerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/containers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(container: IContainer): Observable<EntityResponseType> {
    return this.http.post<IContainer>(this.resourceUrl, container, { observe: 'response' });
  }

  update(container: IContainer): Observable<EntityResponseType> {
    return this.http.put<IContainer>(`${this.resourceUrl}/${getContainerIdentifier(container) as number}`, container, {
      observe: 'response',
    });
  }

  partialUpdate(container: IContainer): Observable<EntityResponseType> {
    return this.http.patch<IContainer>(`${this.resourceUrl}/${getContainerIdentifier(container) as number}`, container, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContainer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContainer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContainerToCollectionIfMissing(
    containerCollection: IContainer[],
    ...containersToCheck: (IContainer | null | undefined)[]
  ): IContainer[] {
    const containers: IContainer[] = containersToCheck.filter(isPresent);
    if (containers.length > 0) {
      const containerCollectionIdentifiers = containerCollection.map(containerItem => getContainerIdentifier(containerItem)!);
      const containersToAdd = containers.filter(containerItem => {
        const containerIdentifier = getContainerIdentifier(containerItem);
        if (containerIdentifier == null || containerCollectionIdentifiers.includes(containerIdentifier)) {
          return false;
        }
        containerCollectionIdentifiers.push(containerIdentifier);
        return true;
      });
      return [...containersToAdd, ...containerCollection];
    }
    return containerCollection;
  }
}
