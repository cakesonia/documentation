import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInterviewType } from 'app/shared/model/interview-type.model';

type EntityResponseType = HttpResponse<IInterviewType>;
type EntityArrayResponseType = HttpResponse<IInterviewType[]>;

@Injectable({ providedIn: 'root' })
export class InterviewTypeService {
  public resourceUrl = SERVER_API_URL + 'api/interview-types';

  constructor(protected http: HttpClient) {}

  create(interviewType: IInterviewType): Observable<EntityResponseType> {
    return this.http.post<IInterviewType>(this.resourceUrl, interviewType, { observe: 'response' });
  }

  update(interviewType: IInterviewType): Observable<EntityResponseType> {
    return this.http.put<IInterviewType>(this.resourceUrl, interviewType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInterviewType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterviewType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
