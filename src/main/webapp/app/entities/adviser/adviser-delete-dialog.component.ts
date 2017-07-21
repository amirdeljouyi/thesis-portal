import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Adviser } from './adviser.model';
import { AdviserPopupService } from './adviser-popup.service';
import { AdviserService } from './adviser.service';

@Component({
    selector: 'jhi-adviser-delete-dialog',
    templateUrl: './adviser-delete-dialog.component.html'
})
export class AdviserDeleteDialogComponent {

    adviser: Adviser;

    constructor(
        private adviserService: AdviserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.adviserService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'adviserListModification',
                content: 'Deleted an adviser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-adviser-delete-popup',
    template: ''
})
export class AdviserDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adviserPopupService: AdviserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.adviserPopupService
                .open(AdviserDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
