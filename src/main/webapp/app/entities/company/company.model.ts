import { ICompanyUser } from 'app/entities/company-user/company-user.model';
import { ICompanyContainer } from 'app/entities/company-container/company-container.model';
import { IOrder } from 'app/entities/order/order.model';
import { IUHFRFIDReader } from 'app/entities/uhfrfid-reader/uhfrfid-reader.model';

export interface ICompany {
  id?: number;
  name?: string;
  address?: string | null;
  phone?: string | null;
  description?: string | null;
  companyUsers?: ICompanyUser[] | null;
  companyContainers?: ICompanyContainer[] | null;
  orders?: IOrder[] | null;
  uhfRFIDReaders?: IUHFRFIDReader[] | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public name?: string,
    public address?: string | null,
    public phone?: string | null,
    public description?: string | null,
    public companyUsers?: ICompanyUser[] | null,
    public companyContainers?: ICompanyContainer[] | null,
    public orders?: IOrder[] | null,
    public uhfRFIDReaders?: IUHFRFIDReader[] | null
  ) {}
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}
