import { BaseEntity } from './../../shared';

const enum Status {
    'INPRORGESS',
    'DISSMISSED',
    'DEFENSED'
}

export class Student implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public status?: Status,
        public yearOfEnter?: number,
        public numOfSupervisor?: number,
        public numOfAdviser?: number,
        public studentNumber?: number,
        public advisers?: BaseEntity[],
        public supervisers?: BaseEntity[],
        public thesis?: BaseEntity,
    ) {
    }
}
