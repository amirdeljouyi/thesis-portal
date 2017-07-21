import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Supervisor } from './supervisor.model';
import { SupervisorService } from './supervisor.service';

@Component({
    selector: 'jhi-supervisor-detail',
    templateUrl: './supervisor-detail.component.html'
})
export class SupervisorDetailComponent implements OnInit, OnDestroy {

    supervisor: Supervisor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private supervisorService: SupervisorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSupervisors();
    }

    load(id) {
        this.supervisorService.find(id).subscribe((supervisor) => {
            this.supervisor = supervisor;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSupervisors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'supervisorListModification',
            (response) => this.load(this.supervisor.id)
        );
    }
}
