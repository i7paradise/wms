import { IUHFRFIDReader } from 'app/entities/uhfrfid-reader/uhfrfid-reader.model';
import { IDoorAntenna } from 'app/entities/door-antenna/door-antenna.model';
import { UHFRFIDAntennaStatus } from 'app/entities/enumerations/uhfrfid-antenna-status.model';

export interface IUHFRFIDAntenna {
  id?: number;
  name?: string;
  outputPower?: number;
  status?: UHFRFIDAntennaStatus;
  uhfRFIDReader?: IUHFRFIDReader | null;
  doorAntennas?: IDoorAntenna[] | null;
}

export class UHFRFIDAntenna implements IUHFRFIDAntenna {
  constructor(
    public id?: number,
    public name?: string,
    public outputPower?: number,
    public status?: UHFRFIDAntennaStatus,
    public uhfRFIDReader?: IUHFRFIDReader | null,
    public doorAntennas?: IDoorAntenna[] | null
  ) {}
}

export function getUHFRFIDAntennaIdentifier(uHFRFIDAntenna: IUHFRFIDAntenna): number | undefined {
  return uHFRFIDAntenna.id;
}
