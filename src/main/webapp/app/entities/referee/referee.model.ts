import { BaseEntity } from './../../shared';

export class Referee implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public thesis?: BaseEntity,
        public professor?: BaseEntity,
    ) {
    }
}
