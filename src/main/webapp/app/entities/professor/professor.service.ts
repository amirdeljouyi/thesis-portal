import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Professor } from './professor.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProfessorService {

    private resourceUrl = 'api/professors';
    private resourceSearchUrl = 'api/_search/professors';

    constructor(private http: Http) { }

    create(professor: Professor): Observable<Professor> {
        const copy = this.convert(professor);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(professor: Professor): Observable<Professor> {
        const copy = this.convert(professor);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Professor> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(professor: Professor): Professor {
        const copy: Professor = Object.assign({}, professor);
        return copy;
    }
}
