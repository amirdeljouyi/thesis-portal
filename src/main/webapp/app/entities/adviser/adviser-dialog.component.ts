import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Adviser } from './adviser.model';
import { AdviserPopupService } from './adviser-popup.service';
import { AdviserService } from './adviser.service';
import { Professor, ProfessorService } from '../professor';
import { Student, StudentService } from '../student';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-adviser-dialog',
    templateUrl: './adviser-dialog.component.html'
})
export class AdviserDialogComponent implements OnInit {

    adviser: Adviser;
    isSaving: boolean;

    professors: Professor[];

    students: Student[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private adviserService: AdviserService,
        private professorService: ProfessorService,
        private studentService: StudentService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.professorService.query()
            .subscribe((res: ResponseWrapper) => { this.professors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.studentService.query()
            .subscribe((res: ResponseWrapper) => { this.students = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.adviser.id !== undefined) {
            this.subscribeToSaveResponse(
                this.adviserService.update(this.adviser));
        } else {
            this.subscribeToSaveResponse(
                this.adviserService.create(this.adviser));
        }
    }

    private subscribeToSaveResponse(result: Observable<Adviser>) {
        result.subscribe((res: Adviser) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Adviser) {
        this.eventManager.broadcast({ name: 'adviserListModification', content: 'OK'});
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

    trackProfessorById(index: number, item: Professor) {
        return item.id;
    }

    trackStudentById(index: number, item: Student) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-adviser-popup',
    template: ''
})
export class AdviserPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adviserPopupService: AdviserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.adviserPopupService
                    .open(AdviserDialogComponent as Component, params['id']);
            } else {
                this.adviserPopupService
                    .open(AdviserDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
