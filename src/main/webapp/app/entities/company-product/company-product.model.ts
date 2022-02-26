import { IContainerCategory } from 'app/entities/container-category/container-category.model';
import { ICompany } from 'app/entities/company/company.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ICompanyProduct {
  id?: number;
  quantity?: number;
  sku?: string | null;
  containerStockingRatio?: number;
  containerCategory?: IContainerCategory | null;
  company?: ICompany | null;
  product?: IProduct | null;
}

export class CompanyProduct implements ICompanyProduct {
  constructor(
    public id?: number,
    public quantity?: number,
    public sku?: string | null,
    public containerStockingRatio?: number,
    public containerCategory?: IContainerCategory | null,
    public company?: ICompany | null,
    public product?: IProduct | null
  ) {}
}

export function getCompanyProductIdentifier(companyProduct: ICompanyProduct): number | undefined {
  return companyProduct.id;
}
