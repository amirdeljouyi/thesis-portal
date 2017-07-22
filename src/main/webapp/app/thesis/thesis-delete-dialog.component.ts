import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Thesis } from './thesis.model';
import { ThesisPopupService } from './thesis-popup.service';
import { ThesisService } from './thesis.service';

@Component({
    selector: 'jhi-thesis-delete-dialog',
    templateUrl: './thesis-delete-dialog.component.html'
})
export class ThesisDeleteDialogComponent {

    thesis: Thesis;

    constructor(
        private thesisService: ThesisService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.thesisService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'thesisListModification',
                content: 'Deleted an thesis'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-thesis-delete-popup',
    template: ''
})
export class ThesisDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private thesisPopupService: ThesisPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.thesisPopupService
                .open(ThesisDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
