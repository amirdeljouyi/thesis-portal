import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../shared';

import { ReportComponent } from './report.component';

export const reportRoute: Routes = [
    {
        path: 'report',
        component: ReportComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Report'
        },
        canActivate: [UserRouteAccessService]
    }
];
