import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Level } from './level.model';
import { LevelService } from './level.service';

@Component({
    selector: 'jhi-level-detail',
    templateUrl: './level-detail.component.html'
})
export class LevelDetailComponent implements OnInit, OnDestroy {

    level: Level;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private levelService: LevelService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLevels();
    }

    load(id) {
        this.levelService.find(id).subscribe((level) => {
            this.level = level;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLevels() {
        this.eventSubscriber = this.eventManager.subscribe(
            'levelListModification',
            (response) => this.load(this.level.id)
        );
    }
}
