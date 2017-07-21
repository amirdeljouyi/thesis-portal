import { BaseEntity } from './../../shared';

export class Adviser implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public startTime?: any,
        public professor?: BaseEntity,
        public student?: BaseEntity,
    ) {
    }
}
