import { IWHLevel } from 'app/entities/wh-level/wh-level.model';

export interface IPosition {
  id?: number;
  name?: string;
  note?: string | null;
  whlevel?: IWHLevel | null;
}

export class Position implements IPosition {
  constructor(public id?: number, public name?: string, public note?: string | null, public whlevel?: IWHLevel | null) {}
}

export function getPositionIdentifier(position: IPosition): number | undefined {
  return position.id;
}
