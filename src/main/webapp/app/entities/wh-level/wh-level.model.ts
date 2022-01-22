import { IBay } from 'app/entities/bay/bay.model';

export interface IWHLevel {
  id?: number;
  name?: string;
  note?: string | null;
  bay?: IBay | null;
}

export class WHLevel implements IWHLevel {
  constructor(public id?: number, public name?: string, public note?: string | null, public bay?: IBay | null) {}
}

export function getWHLevelIdentifier(wHLevel: IWHLevel): number | undefined {
  return wHLevel.id;
}
