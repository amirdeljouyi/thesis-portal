import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Level } from './level.model';
import { LevelPopupService } from './level-popup.service';
import { LevelService } from './level.service';

@Component({
    selector: 'jhi-level-dialog',
    templateUrl: './level-dialog.component.html'
})
export class LevelDialogComponent implements OnInit {

    level: Level;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private levelService: LevelService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.level.id !== undefined) {
            this.subscribeToSaveResponse(
                this.levelService.update(this.level));
        } else {
            this.subscribeToSaveResponse(
                this.levelService.create(this.level));
        }
    }

    private subscribeToSaveResponse(result: Observable<Level>) {
        result.subscribe((res: Level) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Level) {
        this.eventManager.broadcast({ name: 'levelListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-level-popup',
    template: ''
})
export class LevelPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private levelPopupService: LevelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.levelPopupService
                    .open(LevelDialogComponent as Component, params['id']);
            } else {
                this.levelPopupService
                    .open(LevelDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
