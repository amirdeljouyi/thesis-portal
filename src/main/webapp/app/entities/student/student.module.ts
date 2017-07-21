import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThesisPortalSharedModule } from '../../shared';
import {
    StudentService,
    StudentPopupService,
    StudentComponent,
    StudentDetailComponent,
    StudentDialogComponent,
    StudentPopupComponent,
    StudentDeletePopupComponent,
    StudentDeleteDialogComponent,
    studentRoute,
    studentPopupRoute,
} from './';

const ENTITY_STATES = [
    ...studentRoute,
    ...studentPopupRoute,
];

@NgModule({
    imports: [
        ThesisPortalSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        StudentComponent,
        StudentDetailComponent,
        StudentDialogComponent,
        StudentDeleteDialogComponent,
        StudentPopupComponent,
        StudentDeletePopupComponent,
    ],
    entryComponents: [
        StudentComponent,
        StudentDialogComponent,
        StudentPopupComponent,
        StudentDeleteDialogComponent,
        StudentDeletePopupComponent,
    ],
    providers: [
        StudentService,
        StudentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThesisPortalStudentModule {}
