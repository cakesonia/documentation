import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInterview } from 'app/shared/model/interview.model';

type EntityResponseType = HttpResponse<IInterview>;
type EntityArrayResponseType = HttpResponse<IInterview[]>;

@Injectable({ providedIn: 'root' })
export class InterviewService {
  public resourceUrl = SERVER_API_URL + 'api/interviews';

  constructor(protected http: HttpClient) {}

  create(interview: IInterview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(interview);
    return this.http
      .post<IInterview>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(interview: IInterview): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(interview);
    return this.http
      .put<IInterview>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInterview>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInterview[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(interview: IInterview): IInterview {
    const copy: IInterview = Object.assign({}, interview, {
      date: interview.date && interview.date.isValid() ? interview.date.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((interview: IInterview) => {
        interview.date = interview.date ? moment(interview.date) : undefined;
      });
    }
    return res;
  }
}
