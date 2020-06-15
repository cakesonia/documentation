import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInterviewResult } from 'app/shared/model/interview-result.model';

type EntityResponseType = HttpResponse<IInterviewResult>;
type EntityArrayResponseType = HttpResponse<IInterviewResult[]>;

@Injectable({ providedIn: 'root' })
export class InterviewResultService {
  public resourceUrl = SERVER_API_URL + 'api/interview-results';

  constructor(protected http: HttpClient) {}

  create(interviewResult: IInterviewResult): Observable<EntityResponseType> {
    return this.http.post<IInterviewResult>(this.resourceUrl, interviewResult, { observe: 'response' });
  }

  update(interviewResult: IInterviewResult): Observable<EntityResponseType> {
    return this.http.put<IInterviewResult>(this.resourceUrl, interviewResult, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInterviewResult>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterviewResult[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
