import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ThesisPortalStudentModule } from './student/student.module';
import { ThesisPortalProfessorModule } from './professor/professor.module';
import { ThesisPortalLevelModule } from './level/level.module';
import { ThesisPortalThesisModule } from './thesis/thesis.module';
import { ThesisPortalRefereeModule } from './referee/referee.module';
import { ThesisPortalAdviserModule } from './adviser/adviser.module';
import { ThesisPortalSupervisorModule } from './supervisor/supervisor.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ThesisPortalStudentModule,
        ThesisPortalProfessorModule,
        ThesisPortalLevelModule,
        ThesisPortalThesisModule,
        ThesisPortalRefereeModule,
        ThesisPortalAdviserModule,
        ThesisPortalSupervisorModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThesisPortalEntityModule {}
