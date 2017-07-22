import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Thesis } from './thesis.model';
import { ResponseWrapper, createRequestOption } from '../shared';

@Injectable()
export class ThesisService {

    private resourceUrl = 'api/theses';
    private resourceSearchUrl = 'api/_search/theses';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(thesis: Thesis): Observable<Thesis> {
        const copy = this.convert(thesis);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(thesis: Thesis): Observable<Thesis> {
        const copy = this.convert(thesis);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Thesis> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.dayOfDefense = this.dateUtils
            .convertDateTimeFromServer(entity.dayOfDefense);
    }

    private convert(thesis: Thesis): Thesis {
        const copy: Thesis = Object.assign({}, thesis);

        copy.dayOfDefense = this.dateUtils.toDate(thesis.dayOfDefense);
        return copy;
    }
}
