import { IWHRow } from 'app/entities/wh-row/wh-row.model';

export interface IBay {
  id?: number;
  name?: string;
  note?: string | null;
  whrow?: IWHRow | null;
}

export class Bay implements IBay {
  constructor(public id?: number, public name?: string, public note?: string | null, public whrow?: IWHRow | null) {}
}

export function getBayIdentifier(bay: IBay): number | undefined {
  return bay.id;
}
