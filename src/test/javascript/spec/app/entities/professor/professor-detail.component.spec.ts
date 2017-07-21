/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { ThesisPortalTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProfessorDetailComponent } from '../../../../../../main/webapp/app/entities/professor/professor-detail.component';
import { ProfessorService } from '../../../../../../main/webapp/app/entities/professor/professor.service';
import { Professor } from '../../../../../../main/webapp/app/entities/professor/professor.model';

describe('Component Tests', () => {

    describe('Professor Management Detail Component', () => {
        let comp: ProfessorDetailComponent;
        let fixture: ComponentFixture<ProfessorDetailComponent>;
        let service: ProfessorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ThesisPortalTestModule],
                declarations: [ProfessorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProfessorService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProfessorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProfessorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProfessorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Professor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.professor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
