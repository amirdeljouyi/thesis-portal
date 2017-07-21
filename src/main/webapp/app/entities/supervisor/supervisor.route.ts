import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SupervisorComponent } from './supervisor.component';
import { SupervisorDetailComponent } from './supervisor-detail.component';
import { SupervisorPopupComponent } from './supervisor-dialog.component';
import { SupervisorDeletePopupComponent } from './supervisor-delete-dialog.component';

@Injectable()
export class SupervisorResolvePagingParams implements Resolve<any> {

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

export const supervisorRoute: Routes = [
    {
        path: 'supervisor',
        component: SupervisorComponent,
        resolve: {
            'pagingParams': SupervisorResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Supervisors'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'supervisor/:id',
        component: SupervisorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Supervisors'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const supervisorPopupRoute: Routes = [
    {
        path: 'supervisor-new',
        component: SupervisorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Supervisors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'supervisor/:id/edit',
        component: SupervisorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Supervisors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'supervisor/:id/delete',
        component: SupervisorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Supervisors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
