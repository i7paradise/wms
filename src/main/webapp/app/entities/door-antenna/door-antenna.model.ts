import { IDoor } from 'app/entities/door/door.model';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { DoorAntennaType } from 'app/entities/enumerations/door-antenna-type.model';

export interface IDoorAntenna {
  id?: number;
  type?: DoorAntennaType;
  door?: IDoor | null;
  uhfAntenna?: IUHFRFIDAntenna | null;
}

export class DoorAntenna implements IDoorAntenna {
  constructor(public id?: number, public type?: DoorAntennaType, public door?: IDoor | null, public uhfAntenna?: IUHFRFIDAntenna | null) {}
}

export function getDoorAntennaIdentifier(doorAntenna: IDoorAntenna): number | undefined {
  return doorAntenna.id;
}
