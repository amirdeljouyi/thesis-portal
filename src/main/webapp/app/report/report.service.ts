import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { createRequestOption } from "../shared/index";

@Injectable()
export class ReportService {

    private resourceUrl = '/report';
    private supervisorUrl = 'api/supervisors/professor';
    private adviserUrl = 'api/advisers/professor';
    private refereeUrl = 'api/referees/professor';
    private thesisUrl = "api/theses/professor";

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    querySupervisors(id: number, req?: any): Observable<Response>{
        const options = createRequestOption(req);
        return this.http.get(`${this.supervisorUrl}/${id}`,options);
    }

    queryAdvisers(id: number, req?: any): Observable<Response>{
        const options = createRequestOption(req);
        return this.http.get(`${this.adviserUrl}/${id}`,options);
    }

    queryReferees(id: number, req?: any): Observable<Response>{
        const options = createRequestOption(req);
        return this.http.get(`${this.refereeUrl}/${id}`,options);
    }

    queryTheses(id: number, req?: any): Observable<Response>{
        const options = createRequestOption(req);
        return this.http.get(`${this.thesisUrl}/${id}`,options);
    }

    // query(id:number,req: any): Observable<Response> {
    //     const params: URLSearchParams = new URLSearchParams();
    //     params.set('fromDate', req.fromDate);
    //     params.set('toDate', req.toDate);
    //     params.set('page', req.page);
    //     params.set('size', req.size);
    //     params.set('sort', req.sort);

    //     const options = {
    //         search: params
    //     };

    //     return this.http.get(, options);
    // }
}
