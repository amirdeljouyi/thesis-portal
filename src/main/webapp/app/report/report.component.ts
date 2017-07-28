import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ReportService } from './report.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../shared';
import { PaginationConfig } from '../blocks/config/uib-pagination.config';
import { ReportDialogComponent } from './';
import { Professor, ProfessorService } from "../entities/professor";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Thesis, ThesisService } from "../thesis";
import { Referee, RefereeService } from "../entities/referee";
import { Adviser, AdviserService } from "../entities/adviser";
import { Supervisor, SupervisorService } from "../entities/supervisor";

@Component({
    selector: 'jhi-report',
    templateUrl: './report.component.html',
    styleUrls: ['report.scss']
})
export class ReportComponent implements OnInit, OnDestroy {

    professor: Professor;
    theses: Thesis[];
    referees: Referee[];
    advisers: Adviser[];
    supervisors: Supervisor[];
    activeTab: ActiveTab;

    currentAccount: any;
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    reverse: any;

    constructor(
        private reportService: ReportService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        private professorService: ProfessorService,
        private thesisService: ThesisService,
        private adviserService: AdviserService,
        private supervisorService: SupervisorService,
        private refereeService: RefereeService
    ) {
        this.theses = [];
        this.advisers = [];
        this.referees = [];
        this.supervisors = [];

        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
    }

    showDialog() {
        const modalRef = this.modalService.open(ReportDialogComponent);
        modalRef.result.then((result) => {
            // Left blank intentionally, nothing to do here
        }, (reason) => {
            if (reason) {
                this.professor = reason;
                this.activeTab = 0;
            }
        });
    }

    showDetail(tab: ActiveTab) {
        this.activeTab = tab;
        if (this.activeTab == 1) {
            this.thesisService.query({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: ResponseWrapper) => this.onSuccess(1, res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
                );
        } else if (this.activeTab == 2) {
            this.adviserService.query({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: ResponseWrapper) => this.onSuccess(2, res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
                );
        } else if (this.activeTab == 3) {
            this.thesisService.query({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: ResponseWrapper) => this.onSuccess(1, res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
                );
        } else if (this.activeTab == 4) {
            this.thesisService.query({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: ResponseWrapper) => this.onSuccess(1, res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
                );
        }
    }

    loadAll() {
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    transition() {
        this.router.navigate(['/report'], {
            queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/report', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTheses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    // trackId(index: number) {
    //     return item.id;
    // }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInTheses() {
        this.eventSubscriber = this.eventManager.subscribe('reportListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    private onSuccess(num, data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        if (num == 1) 
            for (let i = 0; i < data.length; i++) 
                this.theses.push(data[i]);
        if (num == 2) 
            for (let i = 0; i < data.length; i++) 
                this.supervisors.push(data[i]);
        if (num == 3) 
            for (let i = 0; i < data.length; i++) 
                this.advisers.push(data[i]);
        if (num == 4) 
            for (let i = 0; i < data.length; i++) 
                this.referees.push(data[i]);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

enum ActiveTab {
    'GENERAL',
    'THESIS',
    'SUPERVISER',
    'ADVISER',
    'REFEREE'
}
