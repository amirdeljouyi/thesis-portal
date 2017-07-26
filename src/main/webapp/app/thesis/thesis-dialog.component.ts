import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Thesis } from './thesis.model';
import { ThesisPopupService } from './thesis-popup.service';
import { ThesisService } from './thesis.service';
import { Student, StudentService } from '../entities/student';
import { ResponseWrapper } from '../shared';

@Component({
    selector: 'jhi-thesis-dialog',
    templateUrl: './thesis-dialog.component.html'
})
export class ThesisDialogComponent implements OnInit {

    thesis: Thesis;
    isSaving: boolean;

    students: Student[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private thesisService: ThesisService,
        private studentService: StudentService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.studentService
            .query({filter: 'thesis(title)-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.thesis.student || !this.thesis.student.id) {
                    this.students = res.json;
                } else {
                    this.studentService
                        .find(this.thesis.student.id)
                        .subscribe((subRes: Student) => {
                            this.students = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, thesis, field, isImage) {
        if (event && event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                thesis[field] = base64Data;
                thesis[`${field}ContentType`] = file.type;
            });
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.thesis.id !== undefined) {
            this.subscribeToSaveResponse(
                this.thesisService.update(this.thesis));
        } else {
            this.thesis.numOfReferee = 0;
            this.subscribeToSaveResponse(
                this.thesisService.create(this.thesis));
        }
    }

    private subscribeToSaveResponse(result: Observable<Thesis>) {
        result.subscribe((res: Thesis) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Thesis) {
        this.eventManager.broadcast({ name: 'thesisListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackStudentById(index: number, item: Student) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-thesis-popup',
    template: ''
})
export class ThesisPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private thesisPopupService: ThesisPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.thesisPopupService
                    .open(ThesisDialogComponent as Component, params['id']);
            } else {
                this.thesisPopupService
                    .open(ThesisDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
