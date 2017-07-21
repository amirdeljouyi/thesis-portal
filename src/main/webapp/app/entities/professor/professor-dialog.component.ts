import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Professor } from './professor.model';
import { ProfessorPopupService } from './professor-popup.service';
import { ProfessorService } from './professor.service';
import { Level, LevelService } from '../level';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-professor-dialog',
    templateUrl: './professor-dialog.component.html'
})
export class ProfessorDialogComponent implements OnInit {

    professor: Professor;
    isSaving: boolean;

    levels: Level[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private professorService: ProfessorService,
        private levelService: LevelService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.levelService.query()
            .subscribe((res: ResponseWrapper) => { this.levels = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.professor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.professorService.update(this.professor));
        } else {
            this.subscribeToSaveResponse(
                this.professorService.create(this.professor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Professor>) {
        result.subscribe((res: Professor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Professor) {
        this.eventManager.broadcast({ name: 'professorListModification', content: 'OK'});
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

    trackLevelById(index: number, item: Level) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-professor-popup',
    template: ''
})
export class ProfessorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private professorPopupService: ProfessorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.professorPopupService
                    .open(ProfessorDialogComponent as Component, params['id']);
            } else {
                this.professorPopupService
                    .open(ProfessorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
