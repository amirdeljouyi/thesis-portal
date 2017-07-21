import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager , JhiDataUtils } from 'ng-jhipster';

import { Thesis } from './thesis.model';
import { ThesisService } from './thesis.service';

@Component({
    selector: 'jhi-thesis-detail',
    templateUrl: './thesis-detail.component.html'
})
export class ThesisDetailComponent implements OnInit, OnDestroy {

    thesis: Thesis;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private thesisService: ThesisService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTheses();
    }

    load(id) {
        this.thesisService.find(id).subscribe((thesis) => {
            this.thesis = thesis;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTheses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'thesisListModification',
            (response) => this.load(this.thesis.id)
        );
    }
}
