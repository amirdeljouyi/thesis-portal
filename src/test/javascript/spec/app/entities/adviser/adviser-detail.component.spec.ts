/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ThesisPortalTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AdviserDetailComponent } from '../../../../../../main/webapp/app/entities/adviser/adviser-detail.component';
import { AdviserService } from '../../../../../../main/webapp/app/entities/adviser/adviser.service';
import { Adviser } from '../../../../../../main/webapp/app/entities/adviser/adviser.model';

describe('Component Tests', () => {

    describe('Adviser Management Detail Component', () => {
        let comp: AdviserDetailComponent;
        let fixture: ComponentFixture<AdviserDetailComponent>;
        let service: AdviserService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ThesisPortalTestModule],
                declarations: [AdviserDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AdviserService,
                    JhiEventManager
                ]
            }).overrideTemplate(AdviserDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdviserDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdviserService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Adviser(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.adviser).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
