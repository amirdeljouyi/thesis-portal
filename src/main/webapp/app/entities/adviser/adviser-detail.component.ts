import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Adviser } from './adviser.model';
import { AdviserService } from './adviser.service';

@Component({
    selector: 'jhi-adviser-detail',
    templateUrl: './adviser-detail.component.html'
})
export class AdviserDetailComponent implements OnInit, OnDestroy {

    adviser: Adviser;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private adviserService: AdviserService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAdvisers();
    }

    load(id) {
        this.adviserService.find(id).subscribe((adviser) => {
            this.adviser = adviser;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAdvisers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'adviserListModification',
            (response) => this.load(this.adviser.id)
        );
    }
}
