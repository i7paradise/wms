import { IArea } from 'app/entities/area/area.model';
import { IDoorAntenna } from 'app/entities/door-antenna/door-antenna.model';

export interface IDoor {
  id?: number;
  name?: string;
  area?: IArea | null;
  doorAntennas?: IDoorAntenna[] | null;
}

export class Door implements IDoor {
  constructor(public id?: number, public name?: string, public area?: IArea | null, public doorAntennas?: IDoorAntenna[] | null) {}
}

export function getDoorIdentifier(door: IDoor): number | undefined {
  return door.id;
}
