import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ThesisComponent } from './thesis.component';
import { ThesisDetailComponent } from './thesis-detail.component';
import { ThesisPopupComponent } from './thesis-dialog.component';
import { ThesisDeletePopupComponent } from './thesis-delete-dialog.component';

@Injectable()
export class ThesisResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const thesisRoute: Routes = [
    {
        path: 'thesis',
        component: ThesisComponent,
        resolve: {
            'pagingParams': ThesisResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Theses'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'thesis/:id',
        component: ThesisDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Theses'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const thesisPopupRoute: Routes = [
    {
        path: 'thesis-new',
        component: ThesisPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Theses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'thesis/:id/edit',
        component: ThesisPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Theses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'thesis/:id/delete',
        component: ThesisDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Theses'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
