export interface ICompanyUser {
  id?: number;
}

export class CompanyUser implements ICompanyUser {
  constructor(public id?: number) {}
}

export function getCompanyUserIdentifier(companyUser: ICompanyUser): number | undefined {
  return companyUser.id;
}
