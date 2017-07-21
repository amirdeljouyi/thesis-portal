import { BaseEntity } from './../../shared';

export class Supervisor implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public startTime?: any,
        public professor?: BaseEntity,
        public student?: BaseEntity,
    ) {
    }
}
