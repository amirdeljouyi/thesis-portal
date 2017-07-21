import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Supervisor } from './supervisor.model';
import { SupervisorPopupService } from './supervisor-popup.service';
import { SupervisorService } from './supervisor.service';

@Component({
    selector: 'jhi-supervisor-delete-dialog',
    templateUrl: './supervisor-delete-dialog.component.html'
})
export class SupervisorDeleteDialogComponent {

    supervisor: Supervisor;

    constructor(
        private supervisorService: SupervisorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.supervisorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'supervisorListModification',
                content: 'Deleted an supervisor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-supervisor-delete-popup',
    template: ''
})
export class SupervisorDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private supervisorPopupService: SupervisorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.supervisorPopupService
                .open(SupervisorDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
