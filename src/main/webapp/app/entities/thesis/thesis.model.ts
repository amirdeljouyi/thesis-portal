import { BaseEntity } from './../../shared';

export class Thesis implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public majorTitle?: string,
        public summary?: string,
        public dayOfDefense?: any,
        public locationOfDefense?: string,
        public fileContentType?: string,
        public file?: any,
        public student?: BaseEntity,
        public referees?: BaseEntity[],
    ) {
    }
}
