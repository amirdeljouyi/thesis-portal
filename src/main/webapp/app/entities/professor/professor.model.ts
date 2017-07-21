import { BaseEntity } from './../../shared';

export class Professor implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public job?: string,
        public freeCapacityOfYear?: number,
        public freeCapacityOfTotal?: number,
        public referees?: BaseEntity[],
        public advisers?: BaseEntity[],
        public supervisors?: BaseEntity[],
        public level?: BaseEntity,
    ) {
    }
}
