import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Level } from './level.model';
import { LevelPopupService } from './level-popup.service';
import { LevelService } from './level.service';

@Component({
    selector: 'jhi-level-delete-dialog',
    templateUrl: './level-delete-dialog.component.html'
})
export class LevelDeleteDialogComponent {

    level: Level;

    constructor(
        private levelService: LevelService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.levelService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'levelListModification',
                content: 'Deleted an level'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-level-delete-popup',
    template: ''
})
export class LevelDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private levelPopupService: LevelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.levelPopupService
                .open(LevelDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
