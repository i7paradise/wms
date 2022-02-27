import { ICompany } from 'app/entities/company/company.model';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { UHFRFIDReaderStatus } from 'app/entities/enumerations/uhfrfid-reader-status.model';

export interface IUHFRFIDReader {
  id?: number;
  name?: string;
  ip?: string;
  port?: number;
  status?: UHFRFIDReaderStatus;
  company?: ICompany | null;
  uhfRFIDAntennas?: IUHFRFIDAntenna[] | null;
}

export class UHFRFIDReader implements IUHFRFIDReader {
  constructor(
    public id?: number,
    public name?: string,
    public ip?: string,
    public port?: number,
    public status?: UHFRFIDReaderStatus,
    public company?: ICompany | null,
    public uhfRFIDAntennas?: IUHFRFIDAntenna[] | null
  ) {}
}

export function getUHFRFIDReaderIdentifier(uHFRFIDReader: IUHFRFIDReader): number | undefined {
  return uHFRFIDReader.id;
}
