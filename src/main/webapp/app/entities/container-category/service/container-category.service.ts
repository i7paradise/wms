import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContainerCategory, getContainerCategoryIdentifier } from '../container-category.model';

export type EntityResponseType = HttpResponse<IContainerCategory>;
export type EntityArrayResponseType = HttpResponse<IContainerCategory[]>;

@Injectable({ providedIn: 'root' })
export class ContainerCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/container-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(containerCategory: IContainerCategory): Observable<EntityResponseType> {
    return this.http.post<IContainerCategory>(this.resourceUrl, containerCategory, { observe: 'response' });
  }

  update(containerCategory: IContainerCategory): Observable<EntityResponseType> {
    return this.http.put<IContainerCategory>(
      `${this.resourceUrl}/${getContainerCategoryIdentifier(containerCategory) as number}`,
      containerCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(containerCategory: IContainerCategory): Observable<EntityResponseType> {
    return this.http.patch<IContainerCategory>(
      `${this.resourceUrl}/${getContainerCategoryIdentifier(containerCategory) as number}`,
      containerCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContainerCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContainerCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContainerCategoryToCollectionIfMissing(
    containerCategoryCollection: IContainerCategory[],
    ...containerCategoriesToCheck: (IContainerCategory | null | undefined)[]
  ): IContainerCategory[] {
    const containerCategories: IContainerCategory[] = containerCategoriesToCheck.filter(isPresent);
    if (containerCategories.length > 0) {
      const containerCategoryCollectionIdentifiers = containerCategoryCollection.map(
        containerCategoryItem => getContainerCategoryIdentifier(containerCategoryItem)!
      );
      const containerCategoriesToAdd = containerCategories.filter(containerCategoryItem => {
        const containerCategoryIdentifier = getContainerCategoryIdentifier(containerCategoryItem);
        if (containerCategoryIdentifier == null || containerCategoryCollectionIdentifiers.includes(containerCategoryIdentifier)) {
          return false;
        }
        containerCategoryCollectionIdentifiers.push(containerCategoryIdentifier);
        return true;
      });
      return [...containerCategoriesToAdd, ...containerCategoryCollection];
    }
    return containerCategoryCollection;
  }
}
