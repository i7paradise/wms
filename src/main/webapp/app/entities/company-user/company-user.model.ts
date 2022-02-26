import { IUser } from 'app/entities/user/user.model';
import { ICompany } from 'app/entities/company/company.model';

export interface ICompanyUser {
  id?: number;
  user?: IUser | null;
  company?: ICompany | null;
}

export class CompanyUser implements ICompanyUser {
  constructor(public id?: number, public user?: IUser | null, public company?: ICompany | null) {}
}

export function getCompanyUserIdentifier(companyUser: ICompanyUser): number | undefined {
  return companyUser.id;
}
