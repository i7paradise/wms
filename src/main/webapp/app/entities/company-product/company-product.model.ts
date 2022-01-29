import { IContainer } from 'app/entities/container/container.model';
import { ICompany } from 'app/entities/company/company.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ICompanyProduct {
  id?: number;
  quantity?: number;
  sku?: string | null;
  stockingRatio?: number;
  container?: IContainer | null;
  company?: ICompany | null;
  product?: IProduct | null;
}

export class CompanyProduct implements ICompanyProduct {
  constructor(
    public id?: number,
    public quantity?: number,
    public sku?: string | null,
    public stockingRatio?: number,
    public container?: IContainer | null,
    public company?: ICompany | null,
    public product?: IProduct | null
  ) {}
}

export function getCompanyProductIdentifier(companyProduct: ICompanyProduct): number | undefined {
  return companyProduct.id;
}
