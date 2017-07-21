import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AdviserComponent } from './adviser.component';
import { AdviserDetailComponent } from './adviser-detail.component';
import { AdviserPopupComponent } from './adviser-dialog.component';
import { AdviserDeletePopupComponent } from './adviser-delete-dialog.component';

@Injectable()
export class AdviserResolvePagingParams implements Resolve<any> {

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

export const adviserRoute: Routes = [
    {
        path: 'adviser',
        component: AdviserComponent,
        resolve: {
            'pagingParams': AdviserResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Advisers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'adviser/:id',
        component: AdviserDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Advisers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const adviserPopupRoute: Routes = [
    {
        path: 'adviser-new',
        component: AdviserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Advisers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'adviser/:id/edit',
        component: AdviserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Advisers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'adviser/:id/delete',
        component: AdviserDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Advisers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
