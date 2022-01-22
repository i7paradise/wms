export interface ICompany {
  id?: number;
  name?: string;
  address?: string | null;
  phone?: string | null;
  description?: string | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string | null,
    public phone?: string | null,
    public description?: string | null
  ) {}
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}
