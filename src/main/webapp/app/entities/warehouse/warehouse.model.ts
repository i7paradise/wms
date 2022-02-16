import { ICompany } from 'app/entities/company/company.model';
import { ILocation } from 'app/entities/location/location.model';
import { IArea } from 'app/entities/area/area.model';

export interface IWarehouse {
  id?: number;
  name?: string;
  note?: string | null;
  phone?: string;
  contactPerson?: string;
  company?: ICompany | null;
  locations?: ILocation[] | null;
  areas?: IArea[] | null;
}

export class Warehouse implements IWarehouse {
  constructor(
    public id?: number,
    public name?: string,
    public note?: string | null,
    public phone?: string,
    public contactPerson?: string,
    public company?: ICompany | null,
    public locations?: ILocation[] | null,
    public areas?: IArea[] | null
  ) {}
}

export function getWarehouseIdentifier(warehouse: IWarehouse): number | undefined {
  return warehouse.id;
}
