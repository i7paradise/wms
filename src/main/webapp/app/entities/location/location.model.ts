import { IWarehouse } from 'app/entities/warehouse/warehouse.model';

export interface ILocation {
  id?: number;
  name?: string;
  note?: string | null;
  warehouse?: IWarehouse | null;
}

export class Location implements ILocation {
  constructor(public id?: number, public name?: string, public note?: string | null, public warehouse?: IWarehouse | null) {}
}

export function getLocationIdentifier(location: ILocation): number | undefined {
  return location.id;
}
