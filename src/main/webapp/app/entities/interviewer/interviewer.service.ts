import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInterviewer } from 'app/shared/model/interviewer.model';

type EntityResponseType = HttpResponse<IInterviewer>;
type EntityArrayResponseType = HttpResponse<IInterviewer[]>;

@Injectable({ providedIn: 'root' })
export class InterviewerService {
  public resourceUrl = SERVER_API_URL + 'api/interviewers';

  constructor(protected http: HttpClient) {}

  create(interviewer: IInterviewer): Observable<EntityResponseType> {
    return this.http.post<IInterviewer>(this.resourceUrl, interviewer, { observe: 'response' });
  }

  update(interviewer: IInterviewer): Observable<EntityResponseType> {
    return this.http.put<IInterviewer>(this.resourceUrl, interviewer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInterviewer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInterviewer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
