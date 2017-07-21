import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Student } from './student.model';
import { StudentService } from './student.service';

@Component({
    selector: 'jhi-student-detail',
    templateUrl: './student-detail.component.html'
})
export class StudentDetailComponent implements OnInit, OnDestroy {

    student: Student;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private studentService: StudentService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStudents();
    }

    load(id) {
        this.studentService.find(id).subscribe((student) => {
            this.student = student;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStudents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'studentListModification',
            (response) => this.load(this.student.id)
        );
    }
}
