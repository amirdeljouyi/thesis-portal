import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Referee } from './referee.model';
import { RefereePopupService } from './referee-popup.service';
import { RefereeService } from './referee.service';
import { Thesis, ThesisService } from '../thesis';
import { Professor, ProfessorService } from '../professor';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-referee-dialog',
    templateUrl: './referee-dialog.component.html'
})
export class RefereeDialogComponent implements OnInit {

    referee: Referee;
    isSaving: boolean;

    theses: Thesis[];

    professors: Professor[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private refereeService: RefereeService,
        private thesisService: ThesisService,
        private professorService: ProfessorService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.thesisService.query()
            .subscribe((res: ResponseWrapper) => { this.theses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.professorService.query()
            .subscribe((res: ResponseWrapper) => { this.professors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.referee.id !== undefined) {
            this.subscribeToSaveResponse(
                this.refereeService.update(this.referee));
        } else {
            this.subscribeToSaveResponse(
                this.refereeService.create(this.referee));
        }
    }

    private subscribeToSaveResponse(result: Observable<Referee>) {
        result.subscribe((res: Referee) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Referee) {
        this.eventManager.broadcast({ name: 'refereeListModification', content: 'OK'});
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

    trackProfessorById(index: number, item: Professor) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-referee-popup',
    template: ''
})
export class RefereePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private refereePopupService: RefereePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.refereePopupService
                    .open(RefereeDialogComponent as Component, params['id']);
            } else {
                this.refereePopupService
                    .open(RefereeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
