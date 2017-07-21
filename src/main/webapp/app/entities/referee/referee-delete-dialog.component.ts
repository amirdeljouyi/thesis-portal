import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Referee } from './referee.model';
import { RefereePopupService } from './referee-popup.service';
import { RefereeService } from './referee.service';

@Component({
    selector: 'jhi-referee-delete-dialog',
    templateUrl: './referee-delete-dialog.component.html'
})
export class RefereeDeleteDialogComponent {

    referee: Referee;

    constructor(
        private refereeService: RefereeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.refereeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'refereeListModification',
                content: 'Deleted an referee'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-referee-delete-popup',
    template: ''
})
export class RefereeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private refereePopupService: RefereePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.refereePopupService
                .open(RefereeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
