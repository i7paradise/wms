import { ICompany } from 'app/entities/company/company.model';
import { UHFRFIDReaderStatus } from 'app/entities/enumerations/uhfrfid-reader-status.model';

export interface IUHFRFIDReader {
  id?: number;
  name?: string;
  ip?: string;
  port?: number;
  status?: UHFRFIDReaderStatus;
  company?: ICompany | null;
}

export class UHFRFIDReader implements IUHFRFIDReader {
  constructor(
    public id?: number,
    public name?: string,
    public ip?: string,
    public port?: number,
    public status?: UHFRFIDReaderStatus,
    public company?: ICompany | null
  ) {}
}

export function getUHFRFIDReaderIdentifier(uHFRFIDReader: IUHFRFIDReader): number | undefined {
  return uHFRFIDReader.id;
}
