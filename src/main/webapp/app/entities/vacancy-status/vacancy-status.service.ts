import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVacancyStatus } from 'app/shared/model/vacancy-status.model';

type EntityResponseType = HttpResponse<IVacancyStatus>;
type EntityArrayResponseType = HttpResponse<IVacancyStatus[]>;

@Injectable({ providedIn: 'root' })
export class VacancyStatusService {
  public resourceUrl = SERVER_API_URL + 'api/vacancy-statuses';

  constructor(protected http: HttpClient) {}

  create(vacancyStatus: IVacancyStatus): Observable<EntityResponseType> {
    return this.http.post<IVacancyStatus>(this.resourceUrl, vacancyStatus, { observe: 'response' });
  }

  update(vacancyStatus: IVacancyStatus): Observable<EntityResponseType> {
    return this.http.put<IVacancyStatus>(this.resourceUrl, vacancyStatus, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVacancyStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVacancyStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
