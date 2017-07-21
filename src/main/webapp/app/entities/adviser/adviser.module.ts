import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThesisPortalSharedModule } from '../../shared';
import {
    AdviserService,
    AdviserPopupService,
    AdviserComponent,
    AdviserDetailComponent,
    AdviserDialogComponent,
    AdviserPopupComponent,
    AdviserDeletePopupComponent,
    AdviserDeleteDialogComponent,
    adviserRoute,
    adviserPopupRoute,
    AdviserResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...adviserRoute,
    ...adviserPopupRoute,
];

@NgModule({
    imports: [
        ThesisPortalSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AdviserComponent,
        AdviserDetailComponent,
        AdviserDialogComponent,
        AdviserDeleteDialogComponent,
        AdviserPopupComponent,
        AdviserDeletePopupComponent,
    ],
    entryComponents: [
        AdviserComponent,
        AdviserDialogComponent,
        AdviserPopupComponent,
        AdviserDeleteDialogComponent,
        AdviserDeletePopupComponent,
    ],
    providers: [
        AdviserService,
        AdviserPopupService,
        AdviserResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThesisPortalAdviserModule {}
