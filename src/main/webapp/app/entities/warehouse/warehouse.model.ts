import { ICompany } from 'app/entities/company/company.model';

export interface IWarehouse {
  id?: number;
  name?: string;
  note?: string | null;
  phone?: string;
  contactPerson?: string;
  company?: ICompany | null;
}

export class Warehouse implements IWarehouse {
  constructor(
    public id?: number,
    public name?: string,
    public note?: string | null,
    public phone?: string,
    public contactPerson?: string,
    public company?: ICompany | null
  ) {}
}

export function getWarehouseIdentifier(warehouse: IWarehouse): number | undefined {
  return warehouse.id;
}
