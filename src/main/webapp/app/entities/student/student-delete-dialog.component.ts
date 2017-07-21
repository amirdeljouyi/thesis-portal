import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Student } from './student.model';
import { StudentPopupService } from './student-popup.service';
import { StudentService } from './student.service';

@Component({
    selector: 'jhi-student-delete-dialog',
    templateUrl: './student-delete-dialog.component.html'
})
export class StudentDeleteDialogComponent {

    student: Student;

    constructor(
        private studentService: StudentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.studentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'studentListModification',
                content: 'Deleted an student'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-student-delete-popup',
    template: ''
})
export class StudentDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentPopupService: StudentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.studentPopupService
                .open(StudentDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
