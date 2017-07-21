import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThesisPortalSharedModule } from '../../shared';
import {
    LevelService,
    LevelPopupService,
    LevelComponent,
    LevelDetailComponent,
    LevelDialogComponent,
    LevelPopupComponent,
    LevelDeletePopupComponent,
    LevelDeleteDialogComponent,
    levelRoute,
    levelPopupRoute,
    LevelResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...levelRoute,
    ...levelPopupRoute,
];

@NgModule({
    imports: [
        ThesisPortalSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        LevelComponent,
        LevelDetailComponent,
        LevelDialogComponent,
        LevelDeleteDialogComponent,
        LevelPopupComponent,
        LevelDeletePopupComponent,
    ],
    entryComponents: [
        LevelComponent,
        LevelDialogComponent,
        LevelPopupComponent,
        LevelDeleteDialogComponent,
        LevelDeletePopupComponent,
    ],
    providers: [
        LevelService,
        LevelPopupService,
        LevelResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThesisPortalLevelModule {}
