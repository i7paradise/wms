import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { AreaType } from 'app/entities/enumerations/area-type.model';

export interface IArea {
  id?: number;
  type?: AreaType;
  warehouse?: IWarehouse | null;
}

export class Area implements IArea {
  constructor(public id?: number, public type?: AreaType, public warehouse?: IWarehouse | null) {}
}

export function getAreaIdentifier(area: IArea): number | undefined {
  return area.id;
}
