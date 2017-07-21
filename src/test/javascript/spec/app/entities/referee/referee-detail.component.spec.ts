/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ThesisPortalTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RefereeDetailComponent } from '../../../../../../main/webapp/app/entities/referee/referee-detail.component';
import { RefereeService } from '../../../../../../main/webapp/app/entities/referee/referee.service';
import { Referee } from '../../../../../../main/webapp/app/entities/referee/referee.model';

describe('Component Tests', () => {

    describe('Referee Management Detail Component', () => {
        let comp: RefereeDetailComponent;
        let fixture: ComponentFixture<RefereeDetailComponent>;
        let service: RefereeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ThesisPortalTestModule],
                declarations: [RefereeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RefereeService,
                    JhiEventManager
                ]
            }).overrideTemplate(RefereeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RefereeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RefereeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Referee(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.referee).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
