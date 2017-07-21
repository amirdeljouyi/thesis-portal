import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Referee } from './referee.model';
import { RefereeService } from './referee.service';

@Component({
    selector: 'jhi-referee-detail',
    templateUrl: './referee-detail.component.html'
})
export class RefereeDetailComponent implements OnInit, OnDestroy {

    referee: Referee;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private refereeService: RefereeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInReferees();
    }

    load(id) {
        this.refereeService.find(id).subscribe((referee) => {
            this.referee = referee;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInReferees() {
        this.eventSubscriber = this.eventManager.subscribe(
            'refereeListModification',
            (response) => this.load(this.referee.id)
        );
    }
}
