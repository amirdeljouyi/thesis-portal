import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RefereeComponent } from './referee.component';
import { RefereeDetailComponent } from './referee-detail.component';
import { RefereePopupComponent } from './referee-dialog.component';
import { RefereeDeletePopupComponent } from './referee-delete-dialog.component';

@Injectable()
export class RefereeResolvePagingParams implements Resolve<any> {

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

export const refereeRoute: Routes = [
    {
        path: 'referee',
        component: RefereeComponent,
        resolve: {
            'pagingParams': RefereeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Referees'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'referee/:id',
        component: RefereeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Referees'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const refereePopupRoute: Routes = [
    {
        path: 'referee-new',
        component: RefereePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Referees'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'referee/:id/edit',
        component: RefereePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Referees'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'referee/:id/delete',
        component: RefereeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Referees'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
