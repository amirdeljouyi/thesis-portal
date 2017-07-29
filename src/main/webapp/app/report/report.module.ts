import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThesisPortalSharedModule } from '../shared';
import {
    ReportService,
    ReportComponent,
    ReportDialogComponent,
    reportRoute,
} from './';

const ENTITY_STATES = [
    ...reportRoute,
];

@NgModule({
    imports: [
        ThesisPortalSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ReportComponent,
        ReportDialogComponent
    ],
    entryComponents: [
        ReportComponent,
        ReportDialogComponent
    ],
    providers: [
        ReportService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReportModule {}
