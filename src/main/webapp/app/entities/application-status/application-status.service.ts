import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IApplicationStatus } from 'app/shared/model/application-status.model';

type EntityResponseType = HttpResponse<IApplicationStatus>;
type EntityArrayResponseType = HttpResponse<IApplicationStatus[]>;

@Injectable({ providedIn: 'root' })
export class ApplicationStatusService {
  public resourceUrl = SERVER_API_URL + 'api/application-statuses';

  constructor(protected http: HttpClient) {}

  create(applicationStatus: IApplicationStatus): Observable<EntityResponseType> {
    return this.http.post<IApplicationStatus>(this.resourceUrl, applicationStatus, { observe: 'response' });
  }

  update(applicationStatus: IApplicationStatus): Observable<EntityResponseType> {
    return this.http.put<IApplicationStatus>(this.resourceUrl, applicationStatus, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApplicationStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApplicationStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
