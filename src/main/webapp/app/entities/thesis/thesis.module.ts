import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThesisPortalSharedModule } from '../../shared';
import {
    ThesisService,
    ThesisPopupService,
    ThesisComponent,
    ThesisDetailComponent,
    ThesisDialogComponent,
    ThesisPopupComponent,
    ThesisDeletePopupComponent,
    ThesisDeleteDialogComponent,
    thesisRoute,
    thesisPopupRoute,
    ThesisResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...thesisRoute,
    ...thesisPopupRoute,
];

@NgModule({
    imports: [
        ThesisPortalSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ThesisComponent,
        ThesisDetailComponent,
        ThesisDialogComponent,
        ThesisDeleteDialogComponent,
        ThesisPopupComponent,
        ThesisDeletePopupComponent,
    ],
    entryComponents: [
        ThesisComponent,
        ThesisDialogComponent,
        ThesisPopupComponent,
        ThesisDeleteDialogComponent,
        ThesisDeletePopupComponent,
    ],
    providers: [
        ThesisService,
        ThesisPopupService,
        ThesisResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThesisPortalThesisModule {}
