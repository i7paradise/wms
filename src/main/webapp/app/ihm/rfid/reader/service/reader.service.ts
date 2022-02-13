import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { EntityResponseType } from 'app/entities/uhfrfid-reader/service/uhfrfid-reader.service';
import { IUHFRFIDReader } from 'app/entities/uhfrfid-reader/uhfrfid-reader.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReaderService {

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v1/reader');

  constructor(protected http: HttpClient, 
    protected applicationConfigService: ApplicationConfigService) { }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUHFRFIDReader>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
