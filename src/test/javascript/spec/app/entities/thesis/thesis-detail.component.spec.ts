/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ThesisPortalTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ThesisDetailComponent } from '../../../../../../main/webapp/app/thesis/thesis-detail.component';
import { ThesisService } from '../../../../../../main/webapp/app/thesis/thesis.service';
import { Thesis } from '../../../../../../main/webapp/app/thesis/thesis.model';

describe('Component Tests', () => {

    describe('Thesis Management Detail Component', () => {
        let comp: ThesisDetailComponent;
        let fixture: ComponentFixture<ThesisDetailComponent>;
        let service: ThesisService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ThesisPortalTestModule],
                declarations: [ThesisDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ThesisService,
                    JhiEventManager
                ]
            }).overrideTemplate(ThesisDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ThesisDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ThesisService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Thesis(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.thesis).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
