import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { LevelComponent } from './level.component';
import { LevelDetailComponent } from './level-detail.component';
import { LevelPopupComponent } from './level-dialog.component';
import { LevelDeletePopupComponent } from './level-delete-dialog.component';

@Injectable()
export class LevelResolvePagingParams implements Resolve<any> {

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

export const levelRoute: Routes = [
    {
        path: 'level',
        component: LevelComponent,
        resolve: {
            'pagingParams': LevelResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'level/:id',
        component: LevelDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const levelPopupRoute: Routes = [
    {
        path: 'level-new',
        component: LevelPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'level/:id/edit',
        component: LevelPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'level/:id/delete',
        component: LevelDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Levels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
