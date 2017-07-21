import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudentComponent } from './student.component';
import { StudentDetailComponent } from './student-detail.component';
import { StudentPopupComponent } from './student-dialog.component';
import { StudentDeletePopupComponent } from './student-delete-dialog.component';

export const studentRoute: Routes = [
    {
        path: 'student',
        component: StudentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Students'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'student/:id',
        component: StudentDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Students'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studentPopupRoute: Routes = [
    {
        path: 'student-new',
        component: StudentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Students'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student/:id/edit',
        component: StudentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Students'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'student/:id/delete',
        component: StudentDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Students'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
