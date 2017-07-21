import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThesisPortalSharedModule } from '../../shared';
import {
    RefereeService,
    RefereePopupService,
    RefereeComponent,
    RefereeDetailComponent,
    RefereeDialogComponent,
    RefereePopupComponent,
    RefereeDeletePopupComponent,
    RefereeDeleteDialogComponent,
    refereeRoute,
    refereePopupRoute,
    RefereeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...refereeRoute,
    ...refereePopupRoute,
];

@NgModule({
    imports: [
        ThesisPortalSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RefereeComponent,
        RefereeDetailComponent,
        RefereeDialogComponent,
        RefereeDeleteDialogComponent,
        RefereePopupComponent,
        RefereeDeletePopupComponent,
    ],
    entryComponents: [
        RefereeComponent,
        RefereeDialogComponent,
        RefereePopupComponent,
        RefereeDeleteDialogComponent,
        RefereeDeletePopupComponent,
    ],
    providers: [
        RefereeService,
        RefereePopupService,
        RefereeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThesisPortalRefereeModule {}
