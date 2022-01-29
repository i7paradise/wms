import { IArea } from 'app/entities/area/area.model';

export interface IDoor {
  id?: number;
  name?: string;
  area?: IArea | null;
}

export class Door implements IDoor {
  constructor(public id?: number, public name?: string, public area?: IArea | null) {}
}

export function getDoorIdentifier(door: IDoor): number | undefined {
  return door.id;
}
