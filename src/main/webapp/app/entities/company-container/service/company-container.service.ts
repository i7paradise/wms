import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompanyContainer, getCompanyContainerIdentifier } from '../company-container.model';

export type EntityResponseType = HttpResponse<ICompanyContainer>;
export type EntityArrayResponseType = HttpResponse<ICompanyContainer[]>;

@Injectable({ providedIn: 'root' })
export class CompanyContainerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/company-containers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(companyContainer: ICompanyContainer): Observable<EntityResponseType> {
    return this.http.post<ICompanyContainer>(this.resourceUrl, companyContainer, { observe: 'response' });
  }

  update(companyContainer: ICompanyContainer): Observable<EntityResponseType> {
    return this.http.put<ICompanyContainer>(
      `${this.resourceUrl}/${getCompanyContainerIdentifier(companyContainer) as number}`,
      companyContainer,
      { observe: 'response' }
    );
  }

  partialUpdate(companyContainer: ICompanyContainer): Observable<EntityResponseType> {
    return this.http.patch<ICompanyContainer>(
      `${this.resourceUrl}/${getCompanyContainerIdentifier(companyContainer) as number}`,
      companyContainer,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompanyContainer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompanyContainer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompanyContainerToCollectionIfMissing(
    companyContainerCollection: ICompanyContainer[],
    ...companyContainersToCheck: (ICompanyContainer | null | undefined)[]
  ): ICompanyContainer[] {
    const companyContainers: ICompanyContainer[] = companyContainersToCheck.filter(isPresent);
    if (companyContainers.length > 0) {
      const companyContainerCollectionIdentifiers = companyContainerCollection.map(
        companyContainerItem => getCompanyContainerIdentifier(companyContainerItem)!
      );
      const companyContainersToAdd = companyContainers.filter(companyContainerItem => {
        const companyContainerIdentifier = getCompanyContainerIdentifier(companyContainerItem);
        if (companyContainerIdentifier == null || companyContainerCollectionIdentifiers.includes(companyContainerIdentifier)) {
          return false;
        }
        companyContainerCollectionIdentifiers.push(companyContainerIdentifier);
        return true;
      });
      return [...companyContainersToAdd, ...companyContainerCollection];
    }
    return companyContainerCollection;
  }
}
