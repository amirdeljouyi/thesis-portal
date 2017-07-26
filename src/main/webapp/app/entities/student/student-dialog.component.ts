import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Student } from './student.model';
import { StudentPopupService } from './student-popup.service';
import { StudentService } from './student.service';
import { Thesis, ThesisService } from '../../thesis';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-student-dialog',
    templateUrl: './student-dialog.component.html'
})
export class StudentDialogComponent implements OnInit {

    student: Student;
    isSaving: boolean;

    theses: Thesis[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private studentService: StudentService,
        private thesisService: ThesisService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.thesisService.query()
            .subscribe((res: ResponseWrapper) => { this.theses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.student.id !== undefined) {
            this.subscribeToSaveResponse(
                this.studentService.update(this.student));
        } else {
            this.student.numOfAdviser = 0;
            this.student.numOfSupervisor = 0;
            this.subscribeToSaveResponse(
                this.studentService.create(this.student));
        }
    }

    private subscribeToSaveResponse(result: Observable<Student>) {
        result.subscribe((res: Student) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Student) {
        this.eventManager.broadcast({ name: 'studentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackThesisById(index: number, item: Thesis) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-student-popup',
    template: ''
})
export class StudentPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentPopupService: StudentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.studentPopupService
                    .open(StudentDialogComponent as Component, params['id']);
            } else {
                this.studentPopupService
                    .open(StudentDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
