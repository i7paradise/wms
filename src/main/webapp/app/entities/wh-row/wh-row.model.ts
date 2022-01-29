import { ILocation } from 'app/entities/location/location.model';

export interface IWHRow {
  id?: number;
  name?: string;
  note?: string | null;
  location?: ILocation | null;
}

export class WHRow implements IWHRow {
  constructor(public id?: number, public name?: string, public note?: string | null, public location?: ILocation | null) {}
}

export function getWHRowIdentifier(wHRow: IWHRow): number | undefined {
  return wHRow.id;
}
