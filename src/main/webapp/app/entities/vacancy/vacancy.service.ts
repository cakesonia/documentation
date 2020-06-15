import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVacancy } from 'app/shared/model/vacancy.model';

type EntityResponseType = HttpResponse<IVacancy>;
type EntityArrayResponseType = HttpResponse<IVacancy[]>;

@Injectable({ providedIn: 'root' })
export class VacancyService {
  public resourceUrl = SERVER_API_URL + 'api/vacancies';

  constructor(protected http: HttpClient) {}

  create(vacancy: IVacancy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vacancy);
    return this.http
      .post<IVacancy>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vacancy: IVacancy): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vacancy);
    return this.http
      .put<IVacancy>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVacancy>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVacancy[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(vacancy: IVacancy): IVacancy {
    const copy: IVacancy = Object.assign({}, vacancy, {
      createdDate: vacancy.createdDate && vacancy.createdDate.isValid() ? vacancy.createdDate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vacancy: IVacancy) => {
        vacancy.createdDate = vacancy.createdDate ? moment(vacancy.createdDate) : undefined;
      });
    }
    return res;
  }
}
