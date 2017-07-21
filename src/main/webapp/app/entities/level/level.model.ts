import { BaseEntity } from './../../shared';

const enum ProfessorLevel {
    'PROFESSOR',
    'ASSISTANTPROFESSOR',
    'ASSOCIATEPROFESSOR'
}

export class Level implements BaseEntity {
    constructor(
        public id?: number,
        public level?: ProfessorLevel,
        public capacityOfYear?: number,
        public capacityOfTotal?: number,
    ) {
    }
}
