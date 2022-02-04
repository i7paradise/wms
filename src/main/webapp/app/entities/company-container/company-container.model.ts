import { IContainerCategory } from 'app/entities/container-category/container-category.model';
import { ICompany } from 'app/entities/company/company.model';

export interface ICompanyContainer {
  id?: number;
  rfidTag?: string | null;
  color?: string | null;
  containerCategory?: IContainerCategory | null;
  company?: ICompany | null;
}

export class CompanyContainer implements ICompanyContainer {
  constructor(
    public id?: number,
    public rfidTag?: string | null,
    public color?: string | null,
    public containerCategory?: IContainerCategory | null,
    public company?: ICompany | null
  ) {}
}

export function getCompanyContainerIdentifier(companyContainer: ICompanyContainer): number | undefined {
  return companyContainer.id;
}
