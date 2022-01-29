import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompanyProduct, getCompanyProductIdentifier } from '../company-product.model';

export type EntityResponseType = HttpResponse<ICompanyProduct>;
export type EntityArrayResponseType = HttpResponse<ICompanyProduct[]>;

@Injectable({ providedIn: 'root' })
export class CompanyProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/company-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(companyProduct: ICompanyProduct): Observable<EntityResponseType> {
    return this.http.post<ICompanyProduct>(this.resourceUrl, companyProduct, { observe: 'response' });
  }

  update(companyProduct: ICompanyProduct): Observable<EntityResponseType> {
    return this.http.put<ICompanyProduct>(`${this.resourceUrl}/${getCompanyProductIdentifier(companyProduct) as number}`, companyProduct, {
      observe: 'response',
    });
  }

  partialUpdate(companyProduct: ICompanyProduct): Observable<EntityResponseType> {
    return this.http.patch<ICompanyProduct>(
      `${this.resourceUrl}/${getCompanyProductIdentifier(companyProduct) as number}`,
      companyProduct,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompanyProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompanyProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompanyProductToCollectionIfMissing(
    companyProductCollection: ICompanyProduct[],
    ...companyProductsToCheck: (ICompanyProduct | null | undefined)[]
  ): ICompanyProduct[] {
    const companyProducts: ICompanyProduct[] = companyProductsToCheck.filter(isPresent);
    if (companyProducts.length > 0) {
      const companyProductCollectionIdentifiers = companyProductCollection.map(
        companyProductItem => getCompanyProductIdentifier(companyProductItem)!
      );
      const companyProductsToAdd = companyProducts.filter(companyProductItem => {
        const companyProductIdentifier = getCompanyProductIdentifier(companyProductItem);
        if (companyProductIdentifier == null || companyProductCollectionIdentifiers.includes(companyProductIdentifier)) {
          return false;
        }
        companyProductCollectionIdentifiers.push(companyProductIdentifier);
        return true;
      });
      return [...companyProductsToAdd, ...companyProductCollection];
    }
    return companyProductCollection;
  }
}
