import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompanyUser, getCompanyUserIdentifier } from '../company-user.model';

export type EntityResponseType = HttpResponse<ICompanyUser>;
export type EntityArrayResponseType = HttpResponse<ICompanyUser[]>;

@Injectable({ providedIn: 'root' })
export class CompanyUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/company-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(companyUser: ICompanyUser): Observable<EntityResponseType> {
    return this.http.post<ICompanyUser>(this.resourceUrl, companyUser, { observe: 'response' });
  }

  update(companyUser: ICompanyUser): Observable<EntityResponseType> {
    return this.http.put<ICompanyUser>(`${this.resourceUrl}/${getCompanyUserIdentifier(companyUser) as number}`, companyUser, {
      observe: 'response',
    });
  }

  partialUpdate(companyUser: ICompanyUser): Observable<EntityResponseType> {
    return this.http.patch<ICompanyUser>(`${this.resourceUrl}/${getCompanyUserIdentifier(companyUser) as number}`, companyUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompanyUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompanyUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompanyUserToCollectionIfMissing(
    companyUserCollection: ICompanyUser[],
    ...companyUsersToCheck: (ICompanyUser | null | undefined)[]
  ): ICompanyUser[] {
    const companyUsers: ICompanyUser[] = companyUsersToCheck.filter(isPresent);
    if (companyUsers.length > 0) {
      const companyUserCollectionIdentifiers = companyUserCollection.map(companyUserItem => getCompanyUserIdentifier(companyUserItem)!);
      const companyUsersToAdd = companyUsers.filter(companyUserItem => {
        const companyUserIdentifier = getCompanyUserIdentifier(companyUserItem);
        if (companyUserIdentifier == null || companyUserCollectionIdentifiers.includes(companyUserIdentifier)) {
          return false;
        }
        companyUserCollectionIdentifiers.push(companyUserIdentifier);
        return true;
      });
      return [...companyUsersToAdd, ...companyUserCollection];
    }
    return companyUserCollection;
  }
}
