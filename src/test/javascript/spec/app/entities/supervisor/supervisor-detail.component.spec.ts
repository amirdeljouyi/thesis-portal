/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ThesisPortalTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SupervisorDetailComponent } from '../../../../../../main/webapp/app/entities/supervisor/supervisor-detail.component';
import { SupervisorService } from '../../../../../../main/webapp/app/entities/supervisor/supervisor.service';
import { Supervisor } from '../../../../../../main/webapp/app/entities/supervisor/supervisor.model';

describe('Component Tests', () => {

    describe('Supervisor Management Detail Component', () => {
        let comp: SupervisorDetailComponent;
        let fixture: ComponentFixture<SupervisorDetailComponent>;
        let service: SupervisorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ThesisPortalTestModule],
                declarations: [SupervisorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SupervisorService,
                    JhiEventManager
                ]
            }).overrideTemplate(SupervisorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SupervisorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SupervisorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Supervisor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.supervisor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
