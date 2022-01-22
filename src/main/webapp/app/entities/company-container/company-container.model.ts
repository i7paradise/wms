import { ICompany } from 'app/entities/company/company.model';
import { IContainer } from 'app/entities/container/container.model';

export interface ICompanyContainer {
  id?: number;
  rfidTag?: string | null;
  color?: string | null;
  company?: ICompany | null;
  container?: IContainer | null;
}

export class CompanyContainer implements ICompanyContainer {
  constructor(
    public id?: number,
    public rfidTag?: string | null,
    public color?: string | null,
    public company?: ICompany | null,
    public container?: IContainer | null
  ) {}
}

export function getCompanyContainerIdentifier(companyContainer: ICompanyContainer): number | undefined {
  return companyContainer.id;
}
