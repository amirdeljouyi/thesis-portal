import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Supervisor } from './supervisor.model';
import { SupervisorPopupService } from './supervisor-popup.service';
import { SupervisorService } from './supervisor.service';
import { Professor, ProfessorService } from '../professor';
import { Student, StudentService } from '../student';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-supervisor-dialog',
    templateUrl: './supervisor-dialog.component.html'
})
export class SupervisorDialogComponent implements OnInit {

    supervisor: Supervisor;
    isSaving: boolean;

    professors: Professor[];

    students: Student[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private supervisorService: SupervisorService,
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
        if (this.supervisor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.supervisorService.update(this.supervisor));
        } else {
            this.subscribeToSaveResponse(
                this.supervisorService.create(this.supervisor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Supervisor>) {
        result.subscribe((res: Supervisor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Supervisor) {
        this.eventManager.broadcast({ name: 'supervisorListModification', content: 'OK'});
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
    selector: 'jhi-supervisor-popup',
    template: ''
})
export class SupervisorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private supervisorPopupService: SupervisorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.supervisorPopupService
                    .open(SupervisorDialogComponent as Component, params['id']);
            } else {
                this.supervisorPopupService
                    .open(SupervisorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
