import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThesisPortalSharedModule } from '../../shared';
import {
    SupervisorService,
    SupervisorPopupService,
    SupervisorComponent,
    SupervisorDetailComponent,
    SupervisorDialogComponent,
    SupervisorPopupComponent,
    SupervisorDeletePopupComponent,
    SupervisorDeleteDialogComponent,
    supervisorRoute,
    supervisorPopupRoute,
    SupervisorResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...supervisorRoute,
    ...supervisorPopupRoute,
];

@NgModule({
    imports: [
        ThesisPortalSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SupervisorComponent,
        SupervisorDetailComponent,
        SupervisorDialogComponent,
        SupervisorDeleteDialogComponent,
        SupervisorPopupComponent,
        SupervisorDeletePopupComponent,
    ],
    entryComponents: [
        SupervisorComponent,
        SupervisorDialogComponent,
        SupervisorPopupComponent,
        SupervisorDeleteDialogComponent,
        SupervisorDeletePopupComponent,
    ],
    providers: [
        SupervisorService,
        SupervisorPopupService,
        SupervisorResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThesisPortalSupervisorModule {}
