import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Professor } from './professor.model';
import { ProfessorService } from './professor.service';

@Component({
    selector: 'jhi-professor-detail',
    templateUrl: './professor-detail.component.html'
})
export class ProfessorDetailComponent implements OnInit, OnDestroy {

    professor: Professor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private professorService: ProfessorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProfessors();
    }

    load(id) {
        this.professorService.find(id).subscribe((professor) => {
            this.professor = professor;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProfessors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'professorListModification',
            (response) => this.load(this.professor.id)
        );
    }
}
